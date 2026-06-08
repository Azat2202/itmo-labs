package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, Offer, OfferError}
import zio.IO

trait CarsUnificationConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Offer.CarInfo]]
}

final class StubCarsUnificationConverter extends CarsUnificationConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Offer.CarInfo]] =
    zio.ZIO.succeed {
      val eo = context.externalOffer
      if (eo.mark.isEmpty || eo.model.isEmpty) {
        Result.failure(OfferError.UnificatorFailed)
      } else {
        Result.success(
          Offer.CarInfo(
            mark = eo.mark.toUpperCase,
            model = eo.model.toUpperCase,
            modification = eo.modification,
            complectationId = None,
            equipment = Map.empty
          )
        )
      }
    }
}

object CarsUnificationConverter {
  def stub: CarsUnificationConverter = new StubCarsUnificationConverter()
}
