package com.mxrsoon.volare.collections

import com.mxrsoon.volare.collection.Collection

data class CollectionsUiState(
    val loading: Boolean = false,
    val collections: List<Collection>? = null,
    val showError: Boolean = false,
    val showCreateCollection: Boolean = false
)