package ru.feedprocessor.workflows

import io.temporal.activity.LocalActivityOptions
import io.temporal.workflow.{Workflow, WorkflowInterface, WorkflowMethod}
import ru.feedprocessor.activities.{ConfigurationActivities, FeedActivities, ScheduleObserverActivities}

import java.time.Duration

@WorkflowInterface
trait ScheduleWorkflow {
  @WorkflowMethod
  def run(): Unit
}

final class ScheduleWorkflowImpl extends ScheduleWorkflow {

  private val localActivityOptions: LocalActivityOptions =
    LocalActivityOptions.newBuilder()
      .setStartToCloseTimeout(Duration.ofSeconds(30))
      .build()

  private val feedActivities: FeedActivities =
    Workflow.newLocalActivityStub(classOf[FeedActivities], localActivityOptions)

  private val scheduleObserverActivities: ScheduleObserverActivities =
    Workflow.newLocalActivityStub(classOf[ScheduleObserverActivities], localActivityOptions)

  private val configurationActivities: ConfigurationActivities =
    Workflow.newLocalActivityStub(classOf[ConfigurationActivities], localActivityOptions)

  override def run(): Unit = {
    val maxConcurrentRunningFeeds =
      configurationActivities.getScheduleWorkflowConfiguration.maxConcurrentRunningFeeds
    val runningFeeds = scheduleObserverActivities.countRunningFeedWorkflows()
    Workflow.getMetricsScope.gauge("running_feed_workflows_count").update(runningFeeds.toDouble)
    val freeSlots = maxConcurrentRunningFeeds - runningFeeds
    if (freeSlots > 0) {
      val tasks = feedActivities.getFirstTasks(freeSlots.toInt)
      scheduleObserverActivities.spawnFeedWorkflows(tasks)
    }
  }
}
