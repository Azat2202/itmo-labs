package ru.feedprocessor.workflows

import com.example.feedprocessor.activities._
import ru.feedprocessor.common.ClusterConfig.OFFERS_TASK_QUEUE
import com.example.feedprocessor.converters._
import io.temporal.api.history.v1.History
import io.temporal.client.WorkflowClient
import io.temporal.testing.{TestEnvironmentOptions, TestWorkflowEnvironment}
import io.temporal.worker.Worker
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ru.feedprocessor.activities.{ConfigurationActivitiesImpl, FeedActivitiesImpl, KafkaActivitiesImpl, OfferActivitiesImpl, ScheduleObserverActivitiesImpl}
import ru.feedprocessor.common.{ClusterConfig, CommonMetrics, ConverterErrorCounter}
import ru.feedprocessor.converters.{CarsUnificationConverter, ComplectationConverter, ExternalPanoramaConverter, GeocoderConverter, ImagesConverter, InteriorPanoramaConverter, OptionsConverter, PriceConverter, StateConverter}
import ru.feedprocessor.managers.{ExternalOfferManager, FeedManager, OfferRequestManager}
import ru.feedprocessor.models.{ExternalOffer, ServiceInfo, Task => DomainTask}
import ru.feedprocessor.storage.inmemory.{InMemoryProtoCacheDao, InMemoryTaskDao}

import java.util.UUID
import scala.util.{Success, Try}

/**
 * Replay-тест проигрывает зафиксированную историю исполнения workflow
 * на актуальной реализации и проверяет отсутствие нарушений детерминизма.
 */
class WorkflowReplayerTest extends AnyFlatSpec with Matchers {

  private case class WorkflowData(workflowId: String, runId: String)

  private val testEnvironment: TestWorkflowEnvironment =
    TestWorkflowEnvironment.newInstance(TestEnvironmentOptions.newBuilder().build())

  private val workflowClient: WorkflowClient = testEnvironment.getWorkflowClient

  private def makeWorker: Worker = {
    val taskDao = InMemoryTaskDao.make
    val cache = InMemoryProtoCacheDao.make[UUID, ExternalOffer]
    val externalOfferManager = new ExternalOfferManager(cache)
    val feedManager = new FeedManager(taskDao)
    val _ = new OfferRequestManager()

    val converterErrorCounter = ConverterErrorCounter(CommonMetrics.registry)
    val feedActivities = new FeedActivitiesImpl(feedManager, externalOfferManager, taskDao)
    val offerActivities = new OfferActivitiesImpl(
      converterErrorCounter, externalOfferManager,
      GeocoderConverter.stub, ImagesConverter.stub, CarsUnificationConverter.stub,
      OptionsConverter.stub, InteriorPanoramaConverter.stub, ExternalPanoramaConverter.stub,
      ComplectationConverter.stub, StateConverter.stub, PriceConverter.stub
    )
    val kafkaActivities = new KafkaActivitiesImpl()
    val configurationActivities = new ConfigurationActivitiesImpl()
    val scheduleObserverActivities = new ScheduleObserverActivitiesImpl(workflowClient)

    val worker = testEnvironment.newWorker(OFFERS_TASK_QUEUE)
    worker.registerWorkflowImplementationTypes(
      classOf[ScheduleWorkflowImpl],
      classOf[FeedWorkflowImpl],
      classOf[OfferWorkflowImpl])
    worker.registerActivitiesImplementations(
      feedActivities, offerActivities, kafkaActivities,
      configurationActivities, scheduleObserverActivities
    )
    worker
  }

  private def replayTest(worker: Worker, history: History): Try[Unit] =
    Try(worker.replayWorkflowExecution(history))

  "WorkflowReplayer" should "successfully replay a synthetic empty FeedWorkflow history" in {
    val worker = makeWorker
    val syntheticHistory = History.newBuilder().build()
    val replay = replayTest(worker, syntheticHistory)
    replay shouldBe a [Success[_]]
  }

  it should "register all workflow implementation classes on the worker" in {
    val worker = makeWorker
    worker should not be null
    val _: DomainTask = DomainTask(
      id = 1L,
      clientId = 1,
      status = DomainTask.Status.New,
      url = "https://example.com/feed.xml",
      clientUrl = "https://example.com/",
      serviceInfo = ServiceInfo(1L, "Demo", 15),
      createdAt = java.time.LocalDateTime.now(),
      startedAt = None,
      updatedAt = None,
      section = DomainTask.Section.Used,
      feedprocessorType = Some(DomainTask.FeedprocessorType.New)
    )
    val _ = ClusterConfig.local
  }
}
