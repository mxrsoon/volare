package com.mxrsoon.volare.collections

import com.mxrsoon.volare.collection.Collection

data class CollectionsUiState(
    val loading: Boolean = false,
    val collections: List<Collection>? = null,
    val loadingError: Boolean = false,
    val actionError: Boolean = false,
    val showCollectionCreation: Boolean = false,
    val newCollectionName: String = ""
)