package com.mxrsoon.volare.item.list

import kotlinx.serialization.Serializable

@Serializable
data class ItemsRoute(
    val collectionId: String,
    val collectionName: String
)