package ru.feedprocessor.models

final case class OffersCount(success: Int = 0, failed: Int = 0) {
  def total: Int = success + failed
  def merge(other: OffersCount): OffersCount =
    OffersCount(success + other.success, failed + other.failed)
}

object OffersCount {
  val empty: OffersCount = OffersCount()
}
