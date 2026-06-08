package ru.feedprocessor.converters

import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Context, Offer, OfferError}
import zio.IO

trait GeocoderConverter {
  def convert(context: Context): IO[ApplicationFailure, Result[Offer]]
}

final class StubGeocoderConverter extends GeocoderConverter {
  override def convert(context: Context): IO[ApplicationFailure, Result[Offer]] =
    zio.ZIO.succeed {
      val address = context.externalOffer.address
      if (address.isEmpty) {
        Result.failure(OfferError.UnknownAddress)
      } else {
        val location = Offer.Location(address = address, geoId = Some(1L), poiId = context.client.poiId)
        val offer = Offer.defaultInstance.copy(location = location)
        Result.success(offer)
      }
    }
}

object GeocoderConverter {
  def stub: GeocoderConverter = new StubGeocoderConverter()
}
