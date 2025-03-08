package com.mxrsoon.volare.item

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Saved item model.
 */
@Serializable
data class Item(
    val id: String = "",
    val name: String,
    val creatorId: String,
    val collectionId: String,
    val createdAt: Instant,
    val url: String?
)
