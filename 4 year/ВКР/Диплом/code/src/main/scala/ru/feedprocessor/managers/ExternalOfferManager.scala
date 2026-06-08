package ru.feedprocessor.managers

import ru.feedprocessor.models.{ExternalOffer, UuidWithPosition}
import ru.feedprocessor.storage.ProtoCacheDao
import zio.{Task => ZTask, UIO, ZIO, ZLayer}

import java.io.InputStream
import java.util.UUID
import scala.xml.XML

final class ExternalOfferManager(cache: ProtoCacheDao[UUID, ExternalOffer]) {

  def parse(inputStream: InputStream): UIO[Seq[ExternalOffer]] = ZIO.succeed {
    val xml = XML.load(inputStream)
    (xml \ "car").zipWithIndex.map { case (carNode, idx) =>
      ExternalOffer(
        uuid = UUID.randomUUID(),
        uniqueId = (carNode \ "vin").text,
        vin = (carNode \ "vin").text,
        mark = (carNode \ "mark").text,
        model = (carNode \ "model").text,
        modification = (carNode \ "modification").text,
        year = optInt(carNode, "year").getOrElse(0),
        runKm = optInt(carNode, "run").getOrElse(0),
        priceRub = optLong(carNode, "price").getOrElse(0L),
        color = (carNode \ "color").text,
        section = (carNode \ "section").text,
        address = (carNode \ "address").text,
        extras = (carNode \ "extras" \ "extra").map(_.text),
        images = (carNode \ "images" \ "image").zipWithIndex.map { case (img, i) =>
          ExternalOffer.Image(srcUrl = img.text, order = i)
        },
        interiorPanorama = optText(carNode, "interior_panorama"),
        externalPanorama = optText(carNode, "external_panorama")
      )
    }
  }

  def add(externalOffers: Seq[ExternalOffer]): ZTask[Seq[UuidWithPosition]] = {
    val zipped = externalOffers.zipWithIndex
    val toAdd = zipped.map { case (eo, _) => eo.uuid -> eo }.toMap
    cache.addAll(toAdd).as(zipped.map { case (eo, idx) => UuidWithPosition(eo.uuid, idx) })
  }

  def get(uuid: UUID): ZTask[ExternalOffer] = cache.get(uuid)

  private def optText(node: scala.xml.Node, tag: String): Option[String] =
    (node \ tag).headOption.map(_.text).filter(_.nonEmpty)

  private def optInt(node: scala.xml.Node, tag: String): Option[Int] =
    optText(node, tag).flatMap(s => scala.util.Try(s.toInt).toOption)

  private def optLong(node: scala.xml.Node, tag: String): Option[Long] =
    optText(node, tag).flatMap(s => scala.util.Try(s.toLong).toOption)
}

object ExternalOfferManager {
  val layer: ZLayer[ProtoCacheDao[UUID, ExternalOffer], Nothing, ExternalOfferManager] =
    ZLayer.fromFunction(new ExternalOfferManager(_))
}
