package ru.feedprocessor.managers

import ru.feedprocessor.models.{Offer, OfferError, OffersCount, OffersRequest, Task}
import zio.{Task => ZTask, ZIO, ZLayer}

final class OfferRequestManager {

  def buildOffersRequest(
      task: Task,
      offers: Seq[Offer],
      errors: Seq[OfferError]
  ): ZTask[OffersRequest] = ZIO.succeed {
    val count = OffersCount(success = offers.size, failed = errors.size)
    OffersRequest(taskId = task.id, offers = offers, offersCount = count, errors = errors)
  }
}

object OfferRequestManager {
  val layer: ZLayer[Any, Nothing, OfferRequestManager] = ZLayer.succeed(new OfferRequestManager())
}
