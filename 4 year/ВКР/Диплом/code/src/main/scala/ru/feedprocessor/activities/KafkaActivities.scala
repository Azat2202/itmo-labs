package ru.feedprocessor.activities

import io.temporal.activity.ActivityInterface
import ru.feedprocessor.common.AnnotatedLogger
import ru.feedprocessor.models.{OffersRequest, Task}

@ActivityInterface
trait KafkaActivities {
  def sendOffersRequest(task: Task, offersRequest: OffersRequest): Unit
  def sendEndBatchMessage(taskId: Long, success: Int, failed: Int): Unit
}

final class KafkaActivitiesImpl extends KafkaActivities {
  private val logger = AnnotatedLogger(classOf[KafkaActivitiesImpl])

  override def sendOffersRequest(task: Task, offersRequest: OffersRequest): Unit = {
    logger.info(
      s"Publishing OffersRequest to topic feedprocessor-result " +
        s"(task=${task.id}, offers=${offersRequest.offers.size}, errors=${offersRequest.errors.size})"
    )
  }

  override def sendEndBatchMessage(taskId: Long, success: Int, failed: Int): Unit = {
    logger.info(
      s"End-batch message for task=$taskId: success=$success failed=$failed"
    )
  }
}
