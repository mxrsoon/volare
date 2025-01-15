package com.mxrsoon.volare.collection

import kotlinx.serialization.Serializable

@Serializable
data class CreateCollectionRequest(
    val name: String
)
