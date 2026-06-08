package ru.feedprocessor.common

import ru.feedprocessor.models.Context

final case class WorkerMDC(values: Map[String, String]) {
  def applyToLogger(): Unit = values.foreach { case (k, v) => org.slf4j.MDC.put(k, v) }
  def clearFromLogger(): Unit = values.keys.foreach(org.slf4j.MDC.remove)
}

object WorkerMDC {
  val empty: WorkerMDC = WorkerMDC(Map.empty)

  def fromContext(context: Context): WorkerMDC = WorkerMDC(
    Map(
      "task_id" -> context.task.id.toString,
      "dealer_id" -> context.client.id.toString,
      "offer_id" -> context.externalOffer.uuid.toString,
      "external_offer_unique_id" -> context.externalOffer.uniqueId
    )
  )
}
