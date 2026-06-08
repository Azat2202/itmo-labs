package ru.feedprocessor.converters

import ru.feedprocessor.common.UnsafeRun.unsafeRunRetryableIO
import com.example.feedprocessor.models._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.feedprocessor.models.{Client, Context, ExternalOffer, OfferError, ServiceInfo, Task}

import java.time.LocalDateTime
import java.util.UUID

class GeocoderConverterTest extends AnyFlatSpec with Matchers {

  private val task = Task(
    id = 1L,
    clientId = 1,
    status = Task.Status.New,
    url = "https://example.com/feed.xml",
    clientUrl = "https://example.com/",
    serviceInfo = ServiceInfo(1L, "Demo", 15),
    createdAt = LocalDateTime.now(),
    startedAt = None,
    updatedAt = None,
    section = Task.Section.Used
  )

  private val client = Client(1L, "Demo", "ул. Пушкина д. Колотушкина", Some(123L))

  private def externalOffer(address: String) = ExternalOffer(
    uuid = UUID.randomUUID(),
    uniqueId = "u1",
    vin = "VINPLACEHOLDER1234",
    mark = "Lada", model = "Vesta", modification = "1.6",
    year = 2020, runKm = 10000, priceRub = 800000L,
    color = "white", section = "used", address = address,
    extras = Seq.empty, images = Seq.empty,
    interiorPanorama = None, externalPanorama = None
  )

  "GeocoderConverter" should "produce success result when address is provided" in {
    val converter = GeocoderConverter.stub
    val ctx = Context(task, client, externalOffer("Москва"), useExternalOfferCache = false)
    val result = unsafeRunRetryableIO(converter.convert(ctx))
    result.isSuccess shouldBe true
    result.value.get.location.address shouldBe "Москва"
  }

  it should "produce failure when address is empty" in {
    val converter = GeocoderConverter.stub
    val ctx = Context(task, client, externalOffer(""), useExternalOfferCache = false)
    val result = unsafeRunRetryableIO(converter.convert(ctx))
    result.isSuccess shouldBe false
    result.error.value.code shouldBe OfferError.UnknownAddress.code
  }
}
