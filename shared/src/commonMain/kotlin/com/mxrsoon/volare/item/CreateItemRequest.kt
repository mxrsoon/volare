package com.mxrsoon.volare.item

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val name: String,
    val collectionId: String,
    val url: String?
)
