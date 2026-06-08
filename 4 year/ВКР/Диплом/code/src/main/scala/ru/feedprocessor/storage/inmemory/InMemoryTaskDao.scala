package ru.feedprocessor.storage.inmemory

import cats.data.NonEmptyList
import com.example.feedprocessor.models
import ru.feedprocessor.models.Task
import ru.feedprocessor.models.Task.Status
import ru.feedprocessor.storage.TaskDao
import zio.{ZIO, ZLayer, Task => ZTask}

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import scala.jdk.CollectionConverters._

final class InMemoryTaskDao extends TaskDao {
  private val tasks = new ConcurrentHashMap[Long, Task]()
  private val lastProceeded = new AtomicLong(0L)

  def upsert(task: Task): Unit = {
    tasks.put(task.id, task)
    ()
  }

  override def get(taskId: Long): ZTask[Task] = ZIO.attempt {
    Option(tasks.get(taskId)).getOrElse(throw new NoSuchElementException(s"Task $taskId not found"))
  }

  override def pickLast(limit: Int): ZTask[Seq[Task]] =
    pickLastByOffset(limit, 0L)

  override def pickLastByOffset(limit: Int, offset: Long): ZTask[Seq[Task]] = ZIO.succeed {
    tasks.values.asScala
      .filter(_.status == Status.New)
      .filter(_.id > offset)
      .toSeq
      .sortBy(_.id)
      .take(limit)
  }

  override def pickLastByOffsetForDealers(
      limit: Int, offset: Long, dealerIds: NonEmptyList[Long]
  ): ZTask[Seq[Task]] = ZIO.succeed {
    val whitelist = dealerIds.toList.toSet
    tasks.values.asScala
      .filter(t => t.status == Status.New && t.id > offset && whitelist.contains(t.serviceInfo.dealerId))
      .toSeq
      .sortBy(_.id)
      .take(limit)
  }

  override def pickLastByOffsetExceptDealers(
      limit: Int, offset: Long, dealerBlackListIds: NonEmptyList[Long]
  ): ZTask[Seq[Task]] = ZIO.succeed {
    val blacklist = dealerBlackListIds.toList.toSet
    tasks.values.asScala
      .filter(t => t.status == Status.New && t.id > offset && !blacklist.contains(t.serviceInfo.dealerId))
      .toSeq
      .sortBy(_.id)
      .take(limit)
  }

  override def getLastProceededTaskId: ZTask[Long] = ZIO.succeed(lastProceeded.get())

  override def setLastProceededTaskId(taskId: Long): ZTask[Unit] = ZIO.succeed {
    lastProceeded.set(taskId)
  }

  override def markTaskProcessing(taskId: Long): ZTask[Unit] = updateTask(taskId)(
    _.copy(status = Status.Processing, startedAt = Some(LocalDateTime.now()))
  )

  override def markTaskFailed(taskId: Long, error: String): ZTask[Unit] = updateTask(taskId)(
    _.copy(status = Status.Failure, updatedAt = Some(LocalDateTime.now()))
  )

  override def markTaskSuccess(taskId: Long): ZTask[Unit] = updateTask(taskId)(
    _.copy(status = Status.Success, updatedAt = Some(LocalDateTime.now()))
  )

  private def updateTask(taskId: Long)(transform: Task => Task): ZTask[Unit] = ZIO.attempt {
    Option(tasks.get(taskId)) match {
      case Some(t) => tasks.put(taskId, transform(t)); ()
      case None => throw new NoSuchElementException(s"Task $taskId not found")
    }
  }
}

object InMemoryTaskDao {
  def make: InMemoryTaskDao = new InMemoryTaskDao()
  val layer: ZLayer[Any, Nothing, TaskDao] = ZLayer.succeed(make)
}
