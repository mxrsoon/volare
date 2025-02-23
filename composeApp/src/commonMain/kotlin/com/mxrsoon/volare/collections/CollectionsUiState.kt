package com.mxrsoon.volare.collections

import com.mxrsoon.volare.collection.CollectionListEntry

data class CollectionsUiState(
    val loading: Boolean = false,
    val entries: List<CollectionListEntry>? = null,
    val loadingError: Boolean = false,
    val actionError: Boolean = false,
    val showCollectionCreation: Boolean = false,
    val newCollectionName: String = ""
)