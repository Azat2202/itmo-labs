package ru.feedprocessor.workflows

import ru.feedprocessor.common.ClusterConfig.{FEEDS_TASK_QUEUE, OFFERS_TASK_QUEUE}
import ru.feedprocessor.common.WorkflowSearchAttributes.feedWorkflowSearchAttribute
import com.example.feedprocessor.models._
import io.temporal.activity.{ActivityOptions, LocalActivityOptions}
import io.temporal.api.enums.v1.{ParentClosePolicy, WorkflowIdReusePolicy}
import io.temporal.workflow._
import ru.feedprocessor.activities.{ConfigurationActivities, FeedActivities, KafkaActivities}
import ru.feedprocessor.models.{Client, FeedWorkflowConfiguration, OffersCount, OffersRequest, Task, TaskFailureCause, UuidWithPosition}

import java.time.Duration

final case class BatchArgs(
    uuids: Seq[UuidWithPosition],
    task: Task,
    client: Client,
    offersCount: OffersCount,
    workflowConfiguration: FeedWorkflowConfiguration)

@WorkflowInterface
trait FeedWorkflow {
  @WorkflowMethod
  def run(batchOrTask: Either[BatchArgs, Task]): Unit
}

final class FeedWorkflowImpl extends FeedWorkflow {

  private val feedActivities: FeedActivities = Workflow.newActivityStub(
    classOf[FeedActivities],
    ActivityOptions.newBuilder()
      .setStartToCloseTimeout(Duration.ofMinutes(5))
      .setTaskQueue(FEEDS_TASK_QUEUE)
      .build()
  )

  private val kafkaActivities: KafkaActivities = Workflow.newActivityStub(
    classOf[KafkaActivities],
    ActivityOptions.newBuilder()
      .setStartToCloseTimeout(Duration.ofMinutes(1))
      .setTaskQueue(FEEDS_TASK_QUEUE)
      .build()
  )

  private val configurationActivities: ConfigurationActivities = Workflow.newLocalActivityStub(
    classOf[ConfigurationActivities],
    LocalActivityOptions.newBuilder()
      .setStartToCloseTimeout(Duration.ofSeconds(30))
      .build()
  )

  override def run(batchOrTask: Either[BatchArgs, Task]): Unit = batchOrTask match {
    case Left(batchArgs) => processBatch(batchArgs)
    case Right(task)     => start(task)
  }

  private def start(task: Task): Unit = {
    feedActivities.markTaskProcessing(task.id)
    val workflowConfiguration = configurationActivities.getFeedWorkflowConfiguration

    val uuidsResult = feedActivities.processFeedInputStream(task)
    uuidsResult match {
      case Left(error) =>
        Workflow.newDetachedCancellationScope { () =>
          feedActivities.markTaskFailed(task.id, TaskFailureCause.DownloadFailed)
          ()
        }.run()
        throw new RuntimeException(s"Failed to load feed for task ${task.id}", error)
      case Right(uuids) =>
        val client = Client(
          id = task.serviceInfo.dealerId,
          name = task.serviceInfo.dealerName,
          address = "",
          poiId = None
        )
        processBatch(
          BatchArgs(
            uuids = uuids,
            task = task,
            client = client,
            offersCount = OffersCount.empty,
            workflowConfiguration = workflowConfiguration
          )
        )
    }
  }

  def processBatch(batchArgs: BatchArgs): Unit = {
    val batchSize = batchArgs.workflowConfiguration.batchSize
    val (processNow, processLater) =
      batchArgs.uuids.sortBy(_.position).splitAt(batchSize.toInt)

    val promises = processNow.map { case UuidWithPosition(uuid, idx) =>
      val offerWorkflow = Workflow.newChildWorkflowStub(
        classOf[OfferWorkflow],
        offerWorkflowOptions(batchArgs.task, idx))
      Async.function(offerWorkflow.process,
        idx, batchArgs.task, batchArgs.client, uuid,
        batchArgs.workflowConfiguration.useExternalOfferCache)
    }
    Promise.allOf(promises: _*)
    val entityResults = promises.map(_.get())

    val successful = entityResults.collect { case r if !r.hasErrors => r.offer }
    val errors = entityResults.flatMap(_.errors.toList.flatMap(_.toList))
    val offersCount = batchArgs.offersCount.merge(
      OffersCount(success = successful.size, failed = errors.size)
    )

    val offersRequest = OffersRequest(
      taskId = batchArgs.task.id,
      offers = successful,
      offersCount = offersCount,
      errors = errors
    )
    kafkaActivities.sendOffersRequest(batchArgs.task, offersRequest)

    if (processLater.nonEmpty) {
      val continueAsNewStub = Workflow.newContinueAsNewStub(
        classOf[FeedWorkflow],
        ContinueAsNewOptions.newBuilder()
          .setTypedSearchAttributes(
            feedWorkflowSearchAttribute(batchArgs.task))
          .build())
      continueAsNewStub.run(
        Left(batchArgs.copy(uuids = processLater, offersCount = offersCount)))
    } else {
      kafkaActivities.sendEndBatchMessage(batchArgs.task.id, offersCount.success, offersCount.failed)
      feedActivities.markTaskSuccess(batchArgs.task.id)
    }
  }

  private def offerWorkflowOptions(task: Task, idx: Int): ChildWorkflowOptions =
    ChildWorkflowOptions.newBuilder()
      .setWorkflowId(s"client-${task.id}-offer-$idx")
      .setTaskQueue(OFFERS_TASK_QUEUE)
      .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_REQUEST_CANCEL)
      .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
      .setCancellationType(ChildWorkflowCancellationType.TRY_CANCEL)
      .build()
}

