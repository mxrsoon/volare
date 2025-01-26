package com.mxrsoon.volare.collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View model for the collections screen.
 */
class CollectionsViewModel(
    private val repository: CollectionsRepository = CollectionsRepository()
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
    private fun getCollections() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, collections = null, loadingError = false)
            
            try {
                val response = repository.getAll()
                uiState = uiState.copy(collections = response)
            } catch (error: Throwable) {
                uiState = uiState.copy(loadingError = true)
            } finally {
                uiState = uiState.copy(loading = false)
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
                getCollections()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }
}