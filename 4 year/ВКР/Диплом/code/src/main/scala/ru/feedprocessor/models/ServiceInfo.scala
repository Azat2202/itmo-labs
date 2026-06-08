package ru.feedprocessor.models

final case class ServiceInfo(
    dealerId: Long,
    dealerName: String,
    saleCategoryId: Int,
    resetCache: Boolean = false)
