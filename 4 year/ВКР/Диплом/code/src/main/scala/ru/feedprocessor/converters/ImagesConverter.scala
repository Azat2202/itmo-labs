package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, Offer}
import zio.IO

trait ImagesConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Seq[Offer.Photo]]]
}

final class StubImagesConverter extends ImagesConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Seq[Offer.Photo]]] =
    zio.ZIO.succeed {
      val photos = context.externalOffer.images.map { image =>
        Offer.Photo(
          imageId = java.util.UUID.nameUUIDFromBytes(image.srcUrl.getBytes).toString,
          srcUrl = image.srcUrl,
          order = image.order
        )
      }
      Result.success(photos)
    }
}

object ImagesConverter {
  def stub: ImagesConverter = new StubImagesConverter()
}
