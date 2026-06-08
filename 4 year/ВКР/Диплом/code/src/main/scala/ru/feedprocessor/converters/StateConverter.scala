package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, Offer, OfferError}
import zio.IO

trait StateConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Offer.State]]
}

final class StubStateConverter extends StateConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Offer.State]] =
    zio.ZIO.succeed {
      val eo = context.externalOffer
      if (eo.runKm <= 0) Result.failure(OfferError.EmptyMileage)
      else Result.success(Offer.State(year = eo.year, runKm = eo.runKm))
    }
}

object StateConverter {
  def stub: StateConverter = new StubStateConverter()
}
