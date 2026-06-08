package ru.feedprocessor.models

import java.util.UUID

final case class Offer(
    externalOfferUuid: UUID,
    uniqueId: String,
    vin: String,
    carInfo: Offer.CarInfo,
    state: Offer.State,
    price: Offer.Price,
    location: Offer.Location,
    photos: Seq[Offer.Photo],
    panoramaInterior: Option[String],
    panoramaExterior: Option[String])

object Offer {

  final case class CarInfo(
      mark: String,
      model: String,
      modification: String,
      complectationId: Option[Long],
      equipment: Map[String, Boolean])

  final case class State(
      year: Int,
      runKm: Int)

  final case class Price(
      rub: Long)

  final case class Location(
      address: String,
      geoId: Option[Long],
      poiId: Option[Long])

  final case class Photo(
      imageId: String,
      srcUrl: String,
      order: Int)

  val defaultInstance: Offer =
    Offer(
      externalOfferUuid = new UUID(0L, 0L),
      uniqueId = "",
      vin = "",
      carInfo = CarInfo("", "", "", None, Map.empty),
      state = State(0, 0),
      price = Price(0L),
      location = Location("", None, None),
      photos = Seq.empty,
      panoramaInterior = None,
      panoramaExterior = None)
}
