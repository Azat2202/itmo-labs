package ru.feedprocessor.workflows

import com.example.feedprocessor.activities._
import ru.feedprocessor.common.ClusterConfig.{FEEDS_TASK_QUEUE, OFFERS_TASK_QUEUE}
import com.example.feedprocessor.converters._
import com.example.feedprocessor.models._
import io.temporal.client.{WorkflowClient, WorkflowOptions}
import io.temporal.testing.{TestEnvironmentOptions, TestWorkflowEnvironment}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.feedprocessor.activities.{ConfigurationActivitiesImpl, FeedActivitiesImpl, KafkaActivitiesImpl, OfferActivitiesImpl}
import ru.feedprocessor.common.{CommonMetrics, ConverterErrorCounter}
import ru.feedprocessor.converters.{CarsUnificationConverter, ComplectationConverter, ExternalPanoramaConverter, GeocoderConverter, ImagesConverter, InteriorPanoramaConverter, OptionsConverter, PriceConverter, StateConverter}
import ru.feedprocessor.managers.{ExternalOfferManager, FeedManager}
import ru.feedprocessor.models.{ExternalOffer, ServiceInfo, Task}
import ru.feedprocessor.storage.inmemory.{InMemoryProtoCacheDao, InMemoryTaskDao}

import java.time.LocalDateTime
import java.util.UUID

class FeedWorkflowTest extends AnyFlatSpec with Matchers {

  "FeedWorkflow" should "process a task and finalize it via Kafka stub" in {
    val env: TestWorkflowEnvironment =
      TestWorkflowEnvironment.newInstance(TestEnvironmentOptions.newBuilder().build())
    try {
      val client: WorkflowClient = env.getWorkflowClient

      val taskDao = InMemoryTaskDao.make
      val cache = InMemoryProtoCacheDao.make[UUID, ExternalOffer]
      val externalOfferManager = new ExternalOfferManager(cache)
      val feedManager = new FeedManager(taskDao)
      val counter = ConverterErrorCounter(CommonMetrics.registry)
      val feedActivities = new FeedActivitiesImpl(feedManager, externalOfferManager, taskDao)
      val offerActivities = new OfferActivitiesImpl(
        counter, externalOfferManager,
        GeocoderConverter.stub, ImagesConverter.stub, CarsUnificationConverter.stub,
        OptionsConverter.stub, InteriorPanoramaConverter.stub, ExternalPanoramaConverter.stub,
        ComplectationConverter.stub, StateConverter.stub, PriceConverter.stub
      )
      val kafkaActivities = new KafkaActivitiesImpl()
      val configurationActivities = new ConfigurationActivitiesImpl()

      val feedWorker = env.newWorker(FEEDS_TASK_QUEUE)
      feedWorker.registerWorkflowImplementationTypes(classOf[FeedWorkflowImpl])
      feedWorker.registerActivitiesImplementations(feedActivities, kafkaActivities, configurationActivities)

      val offerWorker = env.newWorker(OFFERS_TASK_QUEUE)
      offerWorker.registerWorkflowImplementationTypes(classOf[OfferWorkflowImpl])
      offerWorker.registerActivitiesImplementations(offerActivities)

      env.start()

      val task = Task(
        id = 1001L,
        clientId = 1,
        status = Task.Status.New,
        url = "https://example.com/feed-1001.xml",
        clientUrl = "https://example.com/",
        serviceInfo = ServiceInfo(dealerId = 42L, dealerName = "Demo dealer", saleCategoryId = 15),
        createdAt = LocalDateTime.now(),
        startedAt = None,
        updatedAt = None,
        section = Task.Section.Used,
        feedprocessorType = Some(Task.FeedprocessorType.New)
      )
      taskDao.upsert(task)

      val options = WorkflowOptions.newBuilder()
        .setWorkflowId(s"feed-${task.id}")
        .setTaskQueue(FEEDS_TASK_QUEUE)
        .build()
      val stub = client.newWorkflowStub(classOf[FeedWorkflow], options)
      stub.run(Right(task))

      succeed
    } finally env.close()
  }
}
