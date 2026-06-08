package ru.feedprocessor.models

import enumeratum._
import enumeratum.EnumEntry.Lowercase

import java.time.LocalDateTime

final case class Task(
    id: Long,
    clientId: Int,
    status: Task.Status,
    url: String,
    clientUrl: String,
    serviceInfo: ServiceInfo,
    createdAt: LocalDateTime,
    startedAt: Option[LocalDateTime],
    updatedAt: Option[LocalDateTime],
    section: Task.Section,
    feedprocessorType: Option[Task.FeedprocessorType] = None) {

  def shouldSendToVos: Boolean =
    feedprocessorType.contains(Task.FeedprocessorType.New) &&
      (status == Task.Status.New || status == Task.Status.Processing)
}

object Task {

  sealed trait Status extends EnumEntry with Lowercase
  object Status extends Enum[Status] {
    case object New extends Status
    case object Processing extends Status
    case object Success extends Status
    case object Failure extends Status

    override val values: IndexedSeq[Status] = findValues
  }

  sealed trait FeedprocessorType extends EnumEntry with Lowercase
  object FeedprocessorType extends Enum[FeedprocessorType] {
    case object New extends FeedprocessorType
    case object Old extends FeedprocessorType

    override val values: IndexedSeq[FeedprocessorType] = findValues
  }

  sealed trait Section extends EnumEntry with Lowercase
  object Section extends Enum[Section] {
    case object New extends Section
    case object Used extends Section

    override val values: IndexedSeq[Section] = findValues
  }
}
