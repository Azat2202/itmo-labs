package ru.feedprocessor.common

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.temporal.client.{WorkflowClient, WorkflowClientOptions}
import io.temporal.common.converter.{DefaultDataConverter, JacksonJsonPayloadConverter}
import io.temporal.serviceclient.{WorkflowServiceStubs, WorkflowServiceStubsOptions}

object WorkflowClientFactory {

  private lazy val objectMapper: ObjectMapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  val converter: DefaultDataConverter = DefaultDataConverter
    .newDefaultInstance()
    .withPayloadConverterOverrides(new JacksonJsonPayloadConverter(objectMapper))

  def create(config: ClusterConfig): WorkflowClient = {
    val serviceStubs = WorkflowServiceStubs.newServiceStubs(
      WorkflowServiceStubsOptions.newBuilder()
        .setTarget(config.target)
        .build()
    )
    val clientOptions = WorkflowClientOptions.newBuilder()
      .setNamespace(config.namespace)
      .setDataConverter(converter)
      .build()
    WorkflowClient.newInstance(serviceStubs, clientOptions)
  }
}
