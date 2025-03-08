package com.mxrsoon.volare.item.list

import com.mxrsoon.volare.item.Item

data class ItemsUiState(
    val loading: Boolean = false,
    val entries: List<Item>? = null,
    val loadingError: Boolean = false,
    val actionError: Boolean = false,
    val showItemCreation: Boolean = false,
    val newItemName: String = ""
)