package ru.feedprocessor.models

final case class OffersRequest(
    taskId: Long,
    offers: Seq[Offer],
    offersCount: OffersCount,
    errors: Seq[OfferError])
