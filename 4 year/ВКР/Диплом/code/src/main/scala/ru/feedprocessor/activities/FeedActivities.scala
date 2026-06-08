package ru.feedprocessor.activities

import ru.feedprocessor.common.RetryConfiguration.{fastRetryFailure, postponeRetryFailure}
import ru.feedprocessor.common.UnsafeRun.{unsafeRunRetryableIO, unsafeRunUIO, unsafeRunZIO}
import io.temporal.activity.{Activity, ActivityInterface}
import ru.feedprocessor.managers.{ExternalOfferManager, FeedManager}
import ru.feedprocessor.models.{Task, TaskFailureCause, UuidWithPosition}
import ru.feedprocessor.storage.TaskDao

@ActivityInterface
trait FeedActivities {
  def getTask(taskId: Long): Task
  def getFirstTasks(limit: Int): Seq[Task]
  def markTaskProcessing(taskId: Long): Unit
  def markTaskFailed(taskId: Long, cause: TaskFailureCause): Unit
  def markTaskSuccess(taskId: Long): Unit
  def processFeedInputStream(task: Task): Either[Throwable, Seq[UuidWithPosition]]
}

final class FeedActivitiesImpl(
    feedManager: FeedManager,
    externalOfferManager: ExternalOfferManager,
    taskDao: TaskDao
) extends FeedActivities {

  override def getTask(taskId: Long): Task = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    unsafeRunRetryableIO(
      feedManager.getTask(taskId)
        .mapError(e => postponeRetryFailure("Failed to get task", e, attempt))
    )
  }

  override def getFirstTasks(limit: Int): Seq[Task] = {
    unsafeRunRetryableIO(
      taskDao.pickLast(limit)
        .mapError(e => fastRetryFailure("Failed to fetch first tasks", e))
    )
  }

  override def markTaskProcessing(taskId: Long): Unit = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    unsafeRunRetryableIO(
      taskDao.markTaskProcessing(taskId)
        .mapError(e => postponeRetryFailure("Failed to mark task processing", e, attempt))
    )
  }

  override def markTaskFailed(taskId: Long, cause: TaskFailureCause): Unit = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    unsafeRunRetryableIO(
      taskDao.markTaskFailed(taskId, cause.entryName)
        .mapError(e => postponeRetryFailure("Failed to mark task failed", e, attempt))
    )
  }

  override def markTaskSuccess(taskId: Long): Unit = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    unsafeRunRetryableIO(
      taskDao.markTaskSuccess(taskId)
        .mapError(e => postponeRetryFailure("Failed to mark task success", e, attempt))
    )
  }

  override def processFeedInputStream(task: Task): Either[Throwable, Seq[UuidWithPosition]] = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    try {
      val feedInputStream = unsafeRunZIO(
        feedManager.getFeedInputStream(task.url),
        s"Failed to download feed for task ${task.id}"
      )
      val externalOffers = unsafeRunUIO(
        externalOfferManager.parse(feedInputStream),
        s"Failed to parse feed for task ${task.id}"
      )
      val uuidsWithPosition = unsafeRunRetryableIO(
        externalOfferManager.add(externalOffers)
          .mapError(e => postponeRetryFailure(s"Failed to cache external offers for task ${task.id}", e, attempt))
      )
      Right(uuidsWithPosition)
    } catch {
      case ex: Throwable => Left(ex)
    }
  }
}
