package ru.feedprocessor.storage

import zio.Task

trait ProtoCacheDao[K, V] {
  def add(key: K, value: V): Task[Unit]
  def addAll(values: Map[K, V]): Task[Unit]
  def get(key: K): Task[V]
  def getAll(keys: Seq[K]): Task[Seq[V]]
}
