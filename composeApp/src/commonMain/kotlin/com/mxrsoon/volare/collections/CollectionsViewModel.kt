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
    fun getCollections() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, loading = true, collections = null)
            
            try {
                val response = repository.getAll()
                uiState = uiState.copy(collections = response)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Dismisses the error dialog.
     */
    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }

    /**
     * Creates a new collection.
     */
    fun createCollection() {
        viewModelScope.launch {
            try {
                val mockedNames = listOf(
                    "Filmes",
                    "Jogos",
                    "Compras"
                )

                repository.create(name = mockedNames.random())
                getCollections()
            } catch (error: Throwable) {
                // TODO: Handle error
            }
        }
    }
}