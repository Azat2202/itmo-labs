package ru.feedprocessor.models

final case class OfferError(
    code: String,
    field: String,
    message: String,
    additionalContext: Option[String] = None)

object OfferError {
  val EmptyMileage: OfferError = OfferError("empty_mileage", "state.run_km", "Run km not provided")
  val InvalidVin: OfferError = OfferError("invalid_vin", "vin", "VIN failed format check")
  val UnknownAddress: OfferError = OfferError("unknown_address", "location.address", "Address not recognized")
  val GeocoderFailed: OfferError = OfferError("geocoder_failed", "location", "Geocoder request failed")
  val UnificatorFailed: OfferError = OfferError("unificator_failed", "car_info", "Unificator request failed")
}
