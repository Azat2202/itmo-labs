package ru.feedprocessor.models

final case class Client(
    id: Long,
    name: String,
    address: String,
    poiId: Option[Long])
