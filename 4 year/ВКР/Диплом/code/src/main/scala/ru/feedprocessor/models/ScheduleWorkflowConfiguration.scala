package ru.feedprocessor.models

final case class ScheduleWorkflowConfiguration(
    maxConcurrentRunningFeeds: Long)

object ScheduleWorkflowConfiguration {
  val default: ScheduleWorkflowConfiguration = ScheduleWorkflowConfiguration(50L)
}
