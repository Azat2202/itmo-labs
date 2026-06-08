package ru.feedprocessor.common

import RetryConfiguration.fastRetryFailure
import io.temporal.failure.ApplicationFailure
import zio.{Cause, Exit, IO, Runtime, Unsafe, ZIO}

object UnsafeRun {
  private val runtime: Runtime[Any] = Runtime.default

  def unsafeRunRetryableIO[A](
      io: IO[ApplicationFailure, A],
      workerMDC: WorkerMDC = WorkerMDC.empty
  ): A = {
    workerMDC.applyToLogger()
    try Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(io) match {
        case Exit.Success(a) => a
        case Exit.Failure(cause: Cause[ApplicationFailure]) =>
          cause.failureOption.foreach(throw _)
          cause.dieOption.foreach(throw _)
          throw cause.squash
      }
    }
    finally workerMDC.clearFromLogger()
  }

  def unsafeRunZIO[A](
      zio: ZIO[Any, Throwable, A],
      message: String,
      workerMDC: WorkerMDC = WorkerMDC.empty
  ): A =
    unsafeRunRetryableIO(
      zio.mapError(t => fastRetryFailure(message, t)),
      workerMDC
    )

  def unsafeRunUIO[A](
      zio: ZIO[Any, Nothing, A],
      message: String,
      workerMDC: WorkerMDC = WorkerMDC.empty
  ): A = {
    workerMDC.applyToLogger()
    try Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(zio) match {
        case Exit.Success(a) => a
        case Exit.Failure(cause) => throw fastRetryFailure(message, cause.squash)
      }
    }
    finally workerMDC.clearFromLogger()
  }
}
