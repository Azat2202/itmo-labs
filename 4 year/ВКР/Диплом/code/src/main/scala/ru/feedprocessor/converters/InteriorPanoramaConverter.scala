package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, OfferNotice}
import zio.IO

trait InteriorPanoramaConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Option[String]]]
}

final class StubInteriorPanoramaConverter extends InteriorPanoramaConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Option[String]]] =
    zio.ZIO.succeed {
      context.externalOffer.interiorPanorama match {
        case Some(url) => Result.success(Some(url))
        case None => Result.warned(None, OfferNotice.NoInteriorPanorama)
      }
    }
}

object InteriorPanoramaConverter {
  def stub: InteriorPanoramaConverter = new StubInteriorPanoramaConverter()
}
