package ru.feedprocessor.common

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

object CommonMetrics {
  lazy val registry: MeterRegistry = new SimpleMeterRegistry()
}
