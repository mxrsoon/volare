package com.mxrsoon.volare.collection

import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.serialization.Serializable

/**
 * Collection model with all fields.
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class Collection(
    val id: String = "",
    val name: String,
    val creatorId: String,
    val createdAt: Instant
)