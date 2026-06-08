package ru.feedprocessor.models

import cats.data.NonEmptyList

final case class OfferResult(
    offer: Offer,
    notices: Seq[OfferNotice],
    errors: Option[NonEmptyList[OfferError]]) {

  def hasErrors: Boolean = errors.isDefined
}

object OfferResult {
  val empty: OfferResult = OfferResult(Offer.defaultInstance, Seq.empty, None)
}
