package ru.feedprocessor.common

import ru.feedprocessor.models.{OfferError, OfferNotice}

final case class Result[+A](
    value: Option[A],
    error: Option[OfferError],
    notices: Seq[OfferNotice] = Seq.empty) {

  def isSuccess: Boolean = error.isEmpty
  def isFailure: Boolean = error.isDefined

  def withNotice(notice: OfferNotice): Result[A] = copy(notices = notices :+ notice)

  def map[B](f: A => B): Result[B] = copy(value = value.map(f))
}

object Result {
  def success[A](value: A): Result[A] = Result(Some(value), None, Seq.empty)
  def failure[A](error: OfferError): Result[A] = Result(None, Some(error), Seq.empty)
  def warned[A](value: A, notice: OfferNotice): Result[A] = Result(Some(value), None, Seq(notice))
}
