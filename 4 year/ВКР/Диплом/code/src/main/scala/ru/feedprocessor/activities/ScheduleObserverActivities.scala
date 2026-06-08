package ru.feedprocessor.activities

import io.temporal.activity.ActivityInterface
import io.temporal.api.enums.v1.WorkflowIdReusePolicy
import io.temporal.client.{WorkflowClient, WorkflowOptions, WorkflowStub}
import ru.feedprocessor.common.{AnnotatedLogger, ClusterConfig, WorkflowSearchAttributes}
import ru.feedprocessor.models.Task
import ru.feedprocessor.workflows.{BatchArgs, FeedWorkflow}

@ActivityInterface
trait ScheduleObserverActivities {
  def countRunningFeedWorkflows(): Long
  def spawnFeedWorkflows(tasks: Seq[Task]): Unit
}

final class ScheduleObserverActivitiesImpl(workflowClient: WorkflowClient) extends ScheduleObserverActivities {
  private val logger = AnnotatedLogger(classOf[ScheduleObserverActivitiesImpl])

  override def countRunningFeedWorkflows(): Long = {
    val request = io.temporal.api.workflowservice.v1.CountWorkflowExecutionsRequest.newBuilder()
      .setNamespace(workflowClient.getOptions.getNamespace)
      .setQuery("WorkflowType = \"FeedWorkflow\" AND ExecutionStatus = \"Running\"")
      .build()
    try {
      val response = workflowClient.getWorkflowServiceStubs.blockingStub().countWorkflowExecutions(request)
      response.getCount
    } catch {
      case _: Throwable => 0L
    }
  }

  override def spawnFeedWorkflows(tasks: Seq[Task]): Unit = {
    tasks.foreach { task =>
      val workflowId = s"feed-${task.id}"
      val options = WorkflowOptions.newBuilder()
        .setWorkflowId(workflowId)
        .setTaskQueue(ClusterConfig.FEEDS_TASK_QUEUE)
        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
        .setTypedSearchAttributes(WorkflowSearchAttributes.feedWorkflowSearchAttribute(task))
        .build()
      val stub = workflowClient.newWorkflowStub(classOf[FeedWorkflow], options)
      val untyped = WorkflowStub.fromTyped(stub)
      val arg: Either[BatchArgs, Task] = Right(task)
      val _ = untyped.start(arg)
      logger.info(s"Spawned FeedWorkflow for task ${task.id}")
    }
  }
}
