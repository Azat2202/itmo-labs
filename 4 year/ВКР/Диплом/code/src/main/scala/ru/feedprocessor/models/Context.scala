package ru.feedprocessor.models

final case class Context(
    task: Task,
    client: Client,
    externalOffer: ExternalOffer,
    useExternalOfferCache: Boolean)
