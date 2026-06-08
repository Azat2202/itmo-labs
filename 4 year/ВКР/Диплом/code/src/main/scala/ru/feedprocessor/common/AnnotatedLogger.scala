package ru.feedprocessor.common

import org.slf4j.{Logger, LoggerFactory}

final class AnnotatedLogger(name: String) {
  private val underlying: Logger = LoggerFactory.getLogger(name)

  def info(message: => String, mdc: WorkerMDC = WorkerMDC.empty): Unit =
    withMdc(mdc)(underlying.info(message))

  def warn(message: => String, mdc: WorkerMDC = WorkerMDC.empty): Unit =
    withMdc(mdc)(underlying.warn(message))

  def error(message: => String, cause: Throwable, mdc: WorkerMDC = WorkerMDC.empty): Unit =
    withMdc(mdc)(underlying.error(message, cause))

  private def withMdc(mdc: WorkerMDC)(action: => Unit): Unit = {
    mdc.applyToLogger()
    try action
    finally mdc.clearFromLogger()
  }
}

object AnnotatedLogger {
  def apply(name: String): AnnotatedLogger = new AnnotatedLogger(name)
  def apply(clazz: Class[_]): AnnotatedLogger = new AnnotatedLogger(clazz.getName)
}
