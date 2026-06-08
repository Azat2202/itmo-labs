package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, OfferNotice}
import zio.IO

trait ExternalPanoramaConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Option[String]]]
}

final class StubExternalPanoramaConverter extends ExternalPanoramaConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Option[String]]] =
    zio.ZIO.succeed {
      context.externalOffer.externalPanorama match {
        case Some(url) => Result.success(Some(url))
        case None => Result.warned(None, OfferNotice.NoExteriorPanorama)
      }
    }
}

object ExternalPanoramaConverter {
  def stub: ExternalPanoramaConverter = new StubExternalPanoramaConverter()
}
