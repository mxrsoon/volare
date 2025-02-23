package com.mxrsoon.volare.collection

import kotlinx.serialization.Serializable

/**
 * Entry in the collection list.
 */
@Serializable
data class CollectionListEntry(
    val collection: Collection,
    val itemCount: Int
)