package com.mxrsoon.volare.item

import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * Saved item model.
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class Item(
    val id: String = "",
    val name: String,
    val creatorId: String,
    val collectionId: String,
    val createdAt: Instant,
    val url: String?
)
