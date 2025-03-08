package com.mxrsoon.volare.item.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mxrsoon.volare.item.CreateItemRequest
import com.mxrsoon.volare.item.Item
import com.mxrsoon.volare.item.ItemRepository
import kotlinx.coroutines.launch

/**
 * View model for the items screen.
 */
class ItemsViewModel(
    private val repository: ItemRepository = ItemRepository()
) : ViewModel() {

    /**
     * The current state of the items screen.
     */
    var uiState by mutableStateOf(ItemsUiState())
        private set

    private var isInitialized: Boolean = false
    private lateinit var collectionId: String

    /**
     * Initializes the view model with the given collection ID.
     */
    fun initialize(collectionId: String) {
        if (!isInitialized) {
            this.collectionId = collectionId
            getItems()

            isInitialized = true
        }
    }

    /**
     * Gets the items available to the user.
     */
    private fun getItems(fromRefresh: Boolean = false) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, entries = null, loadingError = false)

            uiState = uiState.copy(
                loading = !fromRefresh,
                entries = if (fromRefresh) uiState.entries else null,
                loadingError = false,
                refreshing = fromRefresh
            )
            
            try {
                val response = repository.getAll(collectionId)
                uiState = uiState.copy(entries = response)
            } catch (error: Throwable) {
                uiState = uiState.copy(entries = null, loadingError = true)
            } finally {
                uiState = uiState.copy(loading = false, refreshing = false)
            }
        }
    }

    /**
     * Dismisses the action error dialog.
     */
    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }

    /**
     * Show the UI to create a new item.
     */
    fun showItemCreation() {
        uiState = uiState.copy(
            showItemCreation = true,
            newItemName = ""
        )
    }

    /**
     * Dismiss the UI to create a new item.
     */
    fun dismissItemCreation() {
        uiState = uiState.copy(showItemCreation = false)
    }

    /**
     * Set the name of the item being created.
     */
    fun setNewItemName(name: String) {
        uiState = uiState.copy(newItemName = name)
    }

    /**
     * Creates a new item based on user input.
     */
    fun createItem() {
        dismissItemCreation()

        viewModelScope.launch {
            try {
                repository.create(
                    CreateItemRequest(
                        name = uiState.newItemName,
                        collectionId = collectionId,
                        url = null
                    )
                )

                refresh()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Deletes a item.
     */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            try {
                repository.delete(id = item.id)
                refresh()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Refreshes the list of items.
     */
    fun refresh() {
        getItems(fromRefresh = true)
    }
}