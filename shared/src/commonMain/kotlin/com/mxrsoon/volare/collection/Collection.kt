package com.mxrsoon.volare.collection

import kotlinx.serialization.Serializable

/**
 * Collection model with all fields.
 */
@Serializable
data class Collection(
    val id: String = "",
    val name: String,
    val creatorId: String
)