package ru.feedprocessor.storage

import cats.data.NonEmptyList
import com.example.feedprocessor.models
import ru.feedprocessor.models
import zio.Task

trait TaskDao {
  def get(taskId: Long): Task[models.Task]
  def pickLast(limit: Int): Task[Seq[models.Task]]
  def pickLastByOffset(limit: Int, offset: Long): Task[Seq[models.Task]]
  def pickLastByOffsetForDealers(limit: Int, offset: Long, dealerIds: NonEmptyList[Long]): Task[Seq[models.Task]]
  def pickLastByOffsetExceptDealers(limit: Int, offset: Long, dealerBlackListIds: NonEmptyList[Long]): Task[Seq[models.Task]]
  def getLastProceededTaskId: Task[Long]
  def setLastProceededTaskId(taskId: Long): Task[Unit]
  def markTaskProcessing(taskId: Long): Task[Unit]
  def markTaskFailed(taskId: Long, error: String): Task[Unit]
  def markTaskSuccess(taskId: Long): Task[Unit]
}
