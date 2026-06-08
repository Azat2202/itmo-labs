package ru.feedprocessor.common

import io.micrometer.core.instrument.{Counter, MeterRegistry}

import java.util.concurrent.ConcurrentHashMap

final class ConverterErrorCounter(registry: MeterRegistry) {
  private val counters = new ConcurrentHashMap[(String, String), Counter]()

  def inc(converter: String, outcome: String): Unit = {
    val key = (converter, outcome)
    Option(counters.get(key)).getOrElse {
      val c = Counter
        .builder("feedprocessor.converter.result")
        .tag("converter", converter)
        .tag("outcome", outcome)
        .register(registry)
      counters.put(key, c)
      c
    }.increment()
  }
}

object ConverterErrorCounter {
  def apply(registry: MeterRegistry): ConverterErrorCounter = new ConverterErrorCounter(registry)
}
