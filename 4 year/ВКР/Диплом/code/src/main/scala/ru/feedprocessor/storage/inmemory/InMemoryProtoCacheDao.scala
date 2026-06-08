package ru.feedprocessor.storage.inmemory

import ru.feedprocessor.storage.ProtoCacheDao
import zio.{Task => ZTask, ZIO}

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

final class InMemoryProtoCacheDao[K, V] extends ProtoCacheDao[K, V] {
  private val store = new ConcurrentHashMap[K, V]()

  override def add(key: K, value: V): ZTask[Unit] = ZIO.succeed {
    store.put(key, value); ()
  }

  override def addAll(values: Map[K, V]): ZTask[Unit] = ZIO.succeed {
    store.putAll(values.asJava); ()
  }

  override def get(key: K): ZTask[V] = ZIO.attempt {
    Option(store.get(key)).getOrElse(throw new NoSuchElementException(s"Key $key not found"))
  }

  override def getAll(keys: Seq[K]): ZTask[Seq[V]] = ZIO.succeed {
    keys.flatMap(k => Option(store.get(k)))
  }
}

object InMemoryProtoCacheDao {
  def make[K, V]: InMemoryProtoCacheDao[K, V] = new InMemoryProtoCacheDao[K, V]
}
