package ru.feedprocessor.managers

import ru.feedprocessor.models.Task
import ru.feedprocessor.storage.TaskDao
import zio.{Task => ZTask, ZIO, ZLayer}

import java.io.{ByteArrayInputStream, InputStream}

final class FeedManager(taskDao: TaskDao) {

  def getTask(taskId: Long): ZTask[Task] = taskDao.get(taskId)

  def getFeedInputStream(url: String): ZTask[InputStream] = ZIO.attempt {
    val sample = """|<?xml version="1.0" encoding="UTF-8"?>
                    |<cars>
                    |  <car>
                    |    <mark>Lada</mark>
                    |    <model>Vesta</model>
                    |    <vin>XTAGFK330LY000000</vin>
                    |  </car>
                    |</cars>""".stripMargin
    new ByteArrayInputStream(sample.getBytes("UTF-8"))
  }
}

object FeedManager {
  val layer: ZLayer[TaskDao, Nothing, FeedManager] = ZLayer.fromFunction(new FeedManager(_))
}
