package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, OfferNotice}
import zio.IO

trait ComplectationConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Option[Long]]]
}

final class StubComplectationConverter extends ComplectationConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Option[Long]]] =
    zio.ZIO.succeed {
      val eo = context.externalOffer
      if (eo.modification.isEmpty) Result.warned(None, OfferNotice.NoComplectation)
      else Result.success(Some((eo.mark + eo.model + eo.modification).hashCode.toLong & 0xFFFFFFFFL))
    }
}

object ComplectationConverter {
  def stub: ComplectationConverter = new StubComplectationConverter()
}
