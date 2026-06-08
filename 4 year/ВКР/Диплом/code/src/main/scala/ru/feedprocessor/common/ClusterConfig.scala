package ru.feedprocessor.common

final case class ClusterConfig(
    target: String,
    namespace: String,
    feedTaskQueue: String,
    offerTaskQueue: String,
    scheduleTaskQueue: String)

object ClusterConfig {
  val FEEDS_TASK_QUEUE = "feeds"
  val OFFERS_TASK_QUEUE = "offers"
  val SCHEDULE_TASK_QUEUE = "schedule"

  val local: ClusterConfig =
    ClusterConfig(
      target = "127.0.0.1:7233",
      namespace = "default",
      feedTaskQueue = FEEDS_TASK_QUEUE,
      offerTaskQueue = OFFERS_TASK_QUEUE,
      scheduleTaskQueue = SCHEDULE_TASK_QUEUE
    )
}
