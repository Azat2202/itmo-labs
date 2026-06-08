package ru.feedprocessor.models

final case class OfferNotice(
    code: String,
    field: String,
    message: String)

object OfferNotice {
  val NoExteriorPanorama: OfferNotice = OfferNotice("no_exterior_panorama", "panorama_exterior", "Exterior panorama not provided")
  val NoInteriorPanorama: OfferNotice = OfferNotice("no_interior_panorama", "panorama_interior", "Interior panorama not provided")
  val NoComplectation: OfferNotice = OfferNotice("no_complectation", "car_info.complectation_id", "Complectation not resolved")
}
