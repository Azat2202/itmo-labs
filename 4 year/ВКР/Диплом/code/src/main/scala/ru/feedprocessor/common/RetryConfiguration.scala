package ru.feedprocessor.common

import io.temporal.failure.ApplicationFailure

import java.time.Duration

object RetryConfiguration {
  private val NETWORK_FAILURE_TYPE = "network_failure"
  private val WORKER_FAILURE_TYPE = "worker_failure"

  def fastRetryFailure(message: String, cause: Throwable) =
    ApplicationFailure.newFailureWithCause(
      message, NETWORK_FAILURE_TYPE, cause)

  def postponeRetryFailure(message: String, cause: Throwable, attempt: Int) = {
    val maxDelay = Duration.ofMinutes(15)
    val delay = Duration.ofMinutes(3).multipliedBy(attempt)
    val methodDelay = if (delay.compareTo(maxDelay) <= 0) delay else maxDelay
    ApplicationFailure.newFailureWithCauseAndDelay(
      message, WORKER_FAILURE_TYPE, cause, methodDelay)
  }
}
