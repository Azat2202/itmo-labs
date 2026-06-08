package ru.feedprocessor

import com.example.feedprocessor.activities._
import com.example.feedprocessor.common._
import ru.feedprocessor.common.ClusterConfig.{FEEDS_TASK_QUEUE, OFFERS_TASK_QUEUE, SCHEDULE_TASK_QUEUE}
import com.example.feedprocessor.converters._
import com.example.feedprocessor.workflows._
import io.temporal.client.WorkflowClient
import io.temporal.worker.{WorkerFactory, WorkerFactoryOptions, WorkerOptions}
import ru.feedprocessor.activities.{ConfigurationActivitiesImpl, FeedActivitiesImpl, KafkaActivitiesImpl, OfferActivitiesImpl, ScheduleObserverActivitiesImpl}
import ru.feedprocessor.common.{AnnotatedLogger, ClusterConfig, CommonMetrics, ConverterErrorCounter, WorkflowClientFactory}
import ru.feedprocessor.converters.{CarsUnificationConverter, ComplectationConverter, ExternalPanoramaConverter, GeocoderConverter, ImagesConverter, InteriorPanoramaConverter, OptionsConverter, PriceConverter, StateConverter}
import ru.feedprocessor.managers.{ExternalOfferManager, FeedManager, OfferRequestManager}
import ru.feedprocessor.models.ExternalOffer
import ru.feedprocessor.storage.inmemory.{InMemoryProtoCacheDao, InMemoryTaskDao}
import ru.feedprocessor.workflows.{FeedWorkflowImpl, OfferWorkflowImpl, ScheduleWorkflowImpl}

import java.util.UUID

object Main {

  def main(args: Array[String]): Unit = {
    val logger = AnnotatedLogger(getClass.getName)
    val clusterConfig = ClusterConfig.local

    val taskDao = InMemoryTaskDao.make
    val externalOfferCache = InMemoryProtoCacheDao.make[UUID, ExternalOffer]
    val feedManager = new FeedManager(taskDao)
    val externalOfferManager = new ExternalOfferManager(externalOfferCache)
    val _ = new OfferRequestManager()
    val converterErrorCounter = ConverterErrorCounter(CommonMetrics.registry)

    val feedActivities = new FeedActivitiesImpl(feedManager, externalOfferManager, taskDao)
    val offerActivities = new OfferActivitiesImpl(
      converterErrorCounter,
      externalOfferManager,
      GeocoderConverter.stub,
      ImagesConverter.stub,
      CarsUnificationConverter.stub,
      OptionsConverter.stub,
      InteriorPanoramaConverter.stub,
      ExternalPanoramaConverter.stub,
      ComplectationConverter.stub,
      StateConverter.stub,
      PriceConverter.stub
    )
    val kafkaActivities = new KafkaActivitiesImpl()
    val configurationActivities = new ConfigurationActivitiesImpl()

    val workflowClient: WorkflowClient = WorkflowClientFactory.create(clusterConfig)

    val scheduleObserverActivities = new ScheduleObserverActivitiesImpl(workflowClient)

    val workerFactory = WorkerFactory.newInstance(
      workflowClient,
      WorkerFactoryOptions.newBuilder()
        .setWorkflowCacheSize(1200)
        .build()
    )

    val feedWorker = workerFactory.newWorker(FEEDS_TASK_QUEUE, workerOptions)
    val offerWorker = workerFactory.newWorker(OFFERS_TASK_QUEUE, workerOptions)
    val scheduleWorker = workerFactory.newWorker(SCHEDULE_TASK_QUEUE, workerOptions)

    feedWorker.registerWorkflowImplementationTypes(classOf[FeedWorkflowImpl])
    feedWorker.registerActivitiesImplementations(
      feedActivities, kafkaActivities, configurationActivities
    )

    offerWorker.registerWorkflowImplementationTypes(classOf[OfferWorkflowImpl])
    offerWorker.registerActivitiesImplementations(offerActivities)

    scheduleWorker.registerWorkflowImplementationTypes(classOf[ScheduleWorkflowImpl])
    scheduleWorker.registerActivitiesImplementations(
      feedActivities, scheduleObserverActivities, configurationActivities
    )

    workerFactory.start()
    logger.info(s"Workers started; cluster=${clusterConfig.target}, namespace=${clusterConfig.namespace}")

    Runtime.getRuntime.addShutdownHook(new Thread(() => {
      logger.info("Stopping workers ...")
      workerFactory.shutdown()
      workerFactory.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)
    }))

    Thread.currentThread().join()
  }

  private def workerOptions: WorkerOptions =
    WorkerOptions.newBuilder()
      .setMaxConcurrentActivityTaskPollers(50)
      .setMaxConcurrentWorkflowTaskPollers(50)
      .build()
}
