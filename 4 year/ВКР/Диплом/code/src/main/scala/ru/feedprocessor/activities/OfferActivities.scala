package ru.feedprocessor.activities

import ru.feedprocessor.common.RetryConfiguration.postponeRetryFailure
import ru.feedprocessor.common.UnsafeRun.unsafeRunRetryableIO
import com.example.feedprocessor.converters._
import io.temporal.activity.{Activity, ActivityInterface}
import io.temporal.failure.ApplicationFailure
import ru.feedprocessor.common.{ConverterErrorCounter, Result, WorkerMDC}
import ru.feedprocessor.converters.{CarsUnificationConverter, ComplectationConverter, ExternalPanoramaConverter, GeocoderConverter, ImagesConverter, InteriorPanoramaConverter, OptionsConverter, PriceConverter, StateConverter}
import ru.feedprocessor.managers.ExternalOfferManager
import ru.feedprocessor.models
import ru.feedprocessor.models.{Context, ExternalOffer, Offer, OfferError, Task}
import zio.IO

import java.util.UUID

@ActivityInterface
trait OfferActivities {
  def getExternalOffer(uuid: UUID): ExternalOffer
  def validate(externalOffer: ExternalOffer, task: Task): Either[OfferError, ExternalOffer]
  def geocoding(context: Context): Result[Offer]
  def options(context: Context): Result[Map[String, Boolean]]
  def interiorPanorama(context: Context): Result[Option[String]]
  def externalPanorama(context: Context): Result[Option[String]]
  def uploadImages(context: Context): Result[Seq[Offer.Photo]]
  def unify(context: Context): Result[Offer.CarInfo]
  def complectation(context: Context): Result[Option[Long]]
  def state(context: Context): Result[Offer.State]
  def price(context: Context): Result[Offer.Price]
}

final class OfferActivitiesImpl(
    converterErrorCounter: ConverterErrorCounter,
    externalOfferManager: ExternalOfferManager,
    geocoderConverter: GeocoderConverter,
    imagesConverter: ImagesConverter,
    carsUnificationConverter: CarsUnificationConverter,
    optionsConverter: OptionsConverter,
    interiorPanoramaConverter: InteriorPanoramaConverter,
    externalPanoramaConverter: ExternalPanoramaConverter,
    complectationConverter: ComplectationConverter,
    stateConverter: StateConverter,
    priceConverter: PriceConverter
) extends OfferActivities {

  override def getExternalOffer(uuid: UUID): ExternalOffer = {
    val attempt = Activity.getExecutionContext.getInfo.getAttempt
    unsafeRunRetryableIO(
      externalOfferManager.get(uuid)
        .mapError(e => postponeRetryFailure(s"Failed to read external offer $uuid", e, attempt))
    )
  }

  override def validate(externalOffer: ExternalOffer, task: Task): Either[OfferError, ExternalOffer] = {
    if (externalOffer.vin.isEmpty) Left(models.OfferError.InvalidVin)
    else Right(externalOffer)
  }

  override def geocoding(context: Context): Result[Offer] =
    unsafeRunRetryableIO(
      registerConverterResult("geocoding") {
        geocoderConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def options(context: Context): Result[Map[String, Boolean]] =
    unsafeRunRetryableIO(
      registerConverterResult("options") {
        optionsConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def interiorPanorama(context: Context): Result[Option[String]] =
    unsafeRunRetryableIO(
      registerConverterResult("interior_panorama") {
        interiorPanoramaConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def externalPanorama(context: Context): Result[Option[String]] =
    unsafeRunRetryableIO(
      registerConverterResult("external_panorama") {
        externalPanoramaConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def uploadImages(context: Context): Result[Seq[Offer.Photo]] =
    unsafeRunRetryableIO(
      registerConverterResult("upload_images") {
        imagesConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def unify(context: Context): Result[Offer.CarInfo] =
    unsafeRunRetryableIO(
      registerConverterResult("unify") {
        carsUnificationConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def complectation(context: Context): Result[Option[Long]] =
    unsafeRunRetryableIO(
      registerConverterResult("complectation") {
        complectationConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def state(context: Context): Result[Offer.State] =
    unsafeRunRetryableIO(
      registerConverterResult("state") {
        stateConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  override def price(context: Context): Result[Offer.Price] =
    unsafeRunRetryableIO(
      registerConverterResult("price") {
        priceConverter.convert(context)
      },
      workerMDC = WorkerMDC.fromContext(context)
    )

  private def registerConverterResult[A](convertName: String)
                                        (eff: IO[ApplicationFailure, Result[A]]): IO[ApplicationFailure, Result[A]] =
    eff.tapBoth(
      _ => zio.ZIO.succeed(converterErrorCounter.inc(convertName, "retry")),
      {
        case result if result.isSuccess => zio.ZIO.succeed(converterErrorCounter.inc(convertName, "success"))
        case _ => zio.ZIO.succeed(converterErrorCounter.inc(convertName, "error"))
      }
    )
}
