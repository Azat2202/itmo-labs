package ru.feedprocessor.models

import java.util.UUID

final case class ExternalOffer(
    uuid: UUID,
    uniqueId: String,
    vin: String,
    mark: String,
    model: String,
    modification: String,
    year: Int,
    runKm: Int,
    priceRub: Long,
    color: String,
    section: String,
    address: String,
    extras: Seq[String],
    images: Seq[ExternalOffer.Image],
    interiorPanorama: Option[String],
    externalPanorama: Option[String])

object ExternalOffer {
  final case class Image(srcUrl: String, order: Int)
}
