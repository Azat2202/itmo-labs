package ru.feedprocessor.models

final case class FeedWorkflowConfiguration(
    batchSize: Long,
    useExternalOfferCache: Boolean,
    isDevelopment: Boolean)

object FeedWorkflowConfiguration {
  val default: FeedWorkflowConfiguration =
    FeedWorkflowConfiguration(batchSize = 100L, useExternalOfferCache = true, isDevelopment = false)
}
