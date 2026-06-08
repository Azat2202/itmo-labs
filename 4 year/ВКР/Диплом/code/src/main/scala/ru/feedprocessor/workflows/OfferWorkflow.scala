package ru.feedprocessor.workflows

import cats.data.NonEmptyList
import ru.feedprocessor.common.WorkflowSearchAttributes.offerSearchAttributes
import com.example.feedprocessor.models._
import io.temporal.activity.{ActivityCancellationType, ActivityOptions}
import io.temporal.common.RetryOptions
import io.temporal.workflow.{Async, Promise, Workflow, WorkflowInterface, WorkflowMethod}
import ru.feedprocessor.activities.OfferActivities
import ru.feedprocessor.common.Result
import ru.feedprocessor.models.{Client, Context, ExternalOffer, Offer, OfferResult, Task}

import java.time.Duration
import java.util.UUID

@WorkflowInterface
trait OfferWorkflow {
  @WorkflowMethod
  def process(
      idx: Int,
      task: Task,
      client: Client,
      externalOfferUuid: UUID,
      useExternalOfferCache: Boolean): OfferResult
}

final class OfferWorkflowImpl extends OfferWorkflow {

  private val activityOptions: ActivityOptions = ActivityOptions
    .newBuilder()
    .setStartToCloseTimeout(Duration.ofMinutes(1))
    .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
    .setRetryOptions(
      RetryOptions.newBuilder()
        .setInitialInterval(Duration.ofSeconds(10))
        .setBackoffCoefficient(2d)
        .setMaximumInterval(Duration.ofMinutes(10))
        .build())
    .build()

  private val offerActivities: OfferActivities =
    Workflow.newActivityStub(classOf[OfferActivities], activityOptions)

  override def process(
      idx: Int,
      task: Task,
      client: Client,
      externalOfferUuid: UUID,
      useExternalOfferCache: Boolean): OfferResult = {

    val externalOffer = offerActivities.getExternalOffer(externalOfferUuid)
    val updates = offerSearchAttributes(externalOffer.vin, externalOffer.uniqueId)
    Workflow.upsertTypedSearchAttributes(updates: _*)

    val validation = offerActivities.validate(externalOffer, task)
    validation match {
      case Left(error) =>
        OfferResult(
          offer = Offer.defaultInstance.copy(externalOfferUuid = externalOfferUuid),
          notices = Seq.empty,
          errors = Some(NonEmptyList.one(error))
        )
      case Right(eo) =>
        val context = Context(task, client, eo, useExternalOfferCache)
        val geocodingPromise = Async.function[Context, Result[Offer]](
          offerActivities.geocoding, context
        )
        val optionsPromise = Async.function[Context, Result[Map[String, Boolean]]](
          offerActivities.options, context
        )
        val interiorPanoramaPromise = Async.function[Context, Result[Option[String]]](
          offerActivities.interiorPanorama, context
        )
        val externalPanoramaPromise = Async.function[Context, Result[Option[String]]](
          offerActivities.externalPanorama, context
        )
        val uploadImagesPromise = Async.function[Context, Result[Seq[Offer.Photo]]](
          offerActivities.uploadImages, context
        )
        val unifyPromise = Async.function[Context, Result[Offer.CarInfo]](
          offerActivities.unify, context
        )
        val complectationPromise = Async.function[Context, Result[Option[Long]]](
          offerActivities.complectation, context
        )
        val statePromise = Async.function[Context, Result[Offer.State]](
          offerActivities.state, context
        )
        val pricePromise = Async.function[Context, Result[Offer.Price]](
          offerActivities.price, context
        )

        Promise.allOf(
          Seq[Promise[_]](
            geocodingPromise, optionsPromise, interiorPanoramaPromise, externalPanoramaPromise,
            uploadImagesPromise, unifyPromise, complectationPromise, statePromise, pricePromise
          ): _*
        ).get()

        buildOffer(
          externalOfferUuid,
          eo,
          geocodingPromise.get(),
          optionsPromise.get(),
          interiorPanoramaPromise.get(),
          externalPanoramaPromise.get(),
          uploadImagesPromise.get(),
          unifyPromise.get(),
          complectationPromise.get(),
          statePromise.get(),
          pricePromise.get()
        )
    }
  }

  private def buildOffer(
                          externalOfferUuid: UUID,
                          eo: ExternalOffer,
                          geocoding: Result[Offer],
                          options: Result[Map[String, Boolean]],
                          interiorPanorama: Result[Option[String]],
                          externalPanorama: Result[Option[String]],
                          images: Result[Seq[Offer.Photo]],
                          unify: Result[Offer.CarInfo],
                          complectation: Result[Option[Long]],
                          state: Result[Offer.State],
                          price: Result[Offer.Price]
  ): OfferResult = {
    val errors = Seq(geocoding.error, options.error, interiorPanorama.error, externalPanorama.error,
      images.error, unify.error, complectation.error, state.error, price.error).flatten

    val notices = Seq(geocoding.notices, options.notices, interiorPanorama.notices, externalPanorama.notices,
      images.notices, unify.notices, complectation.notices, state.notices, price.notices).flatten

    if (errors.nonEmpty) {
      OfferResult(
        offer = Offer.defaultInstance.copy(externalOfferUuid = externalOfferUuid),
        notices = notices,
        errors = NonEmptyList.fromList(errors.toList)
      )
    } else {
      val carInfo = unify.value.get.copy(
        complectationId = complectation.value.flatten,
        equipment = options.value.getOrElse(Map.empty)
      )
      val offer = Offer(
        externalOfferUuid = externalOfferUuid,
        uniqueId = eo.uniqueId,
        vin = eo.vin,
        carInfo = carInfo,
        state = state.value.get,
        price = price.value.get,
        location = geocoding.value.get.location,
        photos = images.value.getOrElse(Seq.empty),
        panoramaInterior = interiorPanorama.value.flatten,
        panoramaExterior = externalPanorama.value.flatten
      )
      OfferResult(offer, notices, None)
    }
  }
}
