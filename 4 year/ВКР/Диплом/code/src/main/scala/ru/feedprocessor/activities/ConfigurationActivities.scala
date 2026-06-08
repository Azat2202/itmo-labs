package ru.feedprocessor.activities

import io.temporal.activity.ActivityInterface
import ru.feedprocessor.models.{FeedWorkflowConfiguration, ScheduleWorkflowConfiguration}

@ActivityInterface
trait ConfigurationActivities {
  def getFeedWorkflowConfiguration: FeedWorkflowConfiguration
  def getScheduleWorkflowConfiguration: ScheduleWorkflowConfiguration
}

final class ConfigurationActivitiesImpl extends ConfigurationActivities {
  override def getFeedWorkflowConfiguration: FeedWorkflowConfiguration =
    FeedWorkflowConfiguration.default

  override def getScheduleWorkflowConfiguration: ScheduleWorkflowConfiguration =
    ScheduleWorkflowConfiguration.default
}
