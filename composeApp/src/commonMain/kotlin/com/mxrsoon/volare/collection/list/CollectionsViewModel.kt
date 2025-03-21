package com.mxrsoon.volare.collection.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mxrsoon.volare.collection.Collection
import com.mxrsoon.volare.collection.CollectionRepository
import kotlinx.coroutines.launch

/**
 * View model for the collections screen.
 */
class CollectionsViewModel(
    private val repository: CollectionRepository = CollectionRepository()
) : ViewModel() {

    /**
     * The current state of the collections screen.
     */
    var uiState by mutableStateOf(CollectionsUiState())
        private set

    init {
        getCollections()
    }

    /**
     * Gets the collections available to the user.
     */
    private fun getCollections(fromRefresh: Boolean = false) {
        viewModelScope.launch {
            uiState = uiState.copy(
                loading = !fromRefresh,
                entries = if (fromRefresh) uiState.entries else null,
                loadingError = false,
                refreshing = fromRefresh
            )
            
            try {
                val response = repository.getAll()
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
     * Show the UI to create a new collection.
     */
    fun showCollectionCreation() {
        uiState = uiState.copy(
            showCollectionCreation = true,
            newCollectionName = ""
        )
    }

    /**
     * Dismiss the UI to create a new collection.
     */
    fun dismissCollectionCreation() {
        uiState = uiState.copy(showCollectionCreation = false)
    }

    /**
     * Set the name of the collection being created.
     */
    fun setNewCollectionName(name: String) {
        uiState = uiState.copy(newCollectionName = name)
    }

    /**
     * Creates a new collection based on user input.
     */
    fun createCollection() {
        dismissCollectionCreation()

        viewModelScope.launch {
            try {
                repository.create(name = uiState.newCollectionName)
                refresh()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Deletes a collection.
     */
    fun deleteCollection(collection: Collection) {
        viewModelScope.launch {
            try {
                repository.delete(id = collection.id)
                refresh()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    /**
     * Refreshes the list of collections.
     */
    fun refresh() {
        getCollections(fromRefresh = true)
    }
}