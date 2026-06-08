package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, Offer}
import zio.IO

trait PriceConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Offer.Price]]
}

final class StubPriceConverter extends PriceConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Offer.Price]] =
    zio.ZIO.succeed {
      Result.success(Offer.Price(rub = context.externalOffer.priceRub))
    }
}

object PriceConverter {
  def stub: PriceConverter = new StubPriceConverter()
}
