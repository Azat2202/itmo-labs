package ru.feedprocessor.models

import enumeratum._
import enumeratum.EnumEntry.UpperSnakecase

sealed trait TaskFailureCause extends EnumEntry with UpperSnakecase

object TaskFailureCause extends Enum[TaskFailureCause] {
  case object DownloadFailed extends TaskFailureCause
  case object ParseFailed extends TaskFailureCause
  case object SkippedByNewestTask extends TaskFailureCause
  case object NotApplicable extends TaskFailureCause

  override val values: IndexedSeq[TaskFailureCause] = findValues

  val SKIPPED_BY_NEWEST_TASK: TaskFailureCause = SkippedByNewestTask
}
