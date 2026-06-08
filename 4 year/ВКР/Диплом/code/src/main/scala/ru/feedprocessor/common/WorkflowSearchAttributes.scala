package ru.feedprocessor.common

import io.temporal.common.{SearchAttributeKey, SearchAttributeUpdate, SearchAttributes}
import ru.feedprocessor.models.Task

object WorkflowSearchAttributes {
  val TaskId: SearchAttributeKey[java.lang.Long] = SearchAttributeKey.forLong("TaskId")
  val ClientId: SearchAttributeKey[java.lang.Long] = SearchAttributeKey.forLong("ClientId")
  val FeedprocessorType: SearchAttributeKey[String] = SearchAttributeKey.forKeyword("FeedprocessorType")
  val Vin: SearchAttributeKey[String] = SearchAttributeKey.forKeyword("Vin")
  val UniqueId: SearchAttributeKey[String] = SearchAttributeKey.forKeyword("UniqueId")

  def feedWorkflowSearchAttribute(task: Task): SearchAttributes = {
    SearchAttributes.newBuilder()
      .set(TaskId, Long.box(task.id))
      .set(ClientId, Long.box(task.clientId.toLong))
      .set(FeedprocessorType, task.feedprocessorType.fold("unknown")(_.entryName))
      .build()
  }

  def offerSearchAttributes(vin: String, uniqueId: String): Seq[SearchAttributeUpdate[_]] = Seq(
    SearchAttributeUpdate.valueSet(Vin, vin),
    SearchAttributeUpdate.valueSet(UniqueId, uniqueId)
  )
}
