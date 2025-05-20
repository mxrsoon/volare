package com.mxrsoon.volare.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mxrsoon.volare.common.network.setAuthTokens
import kotlinx.coroutines.launch

/**
 * View model for the login screen.
 */
class LoginViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    /**
     * The current state of the login screen.
     */
    var uiState by mutableStateOf(LoginUiState())
        private set

    /**
     * Attempts to log in with the current credentials.
     */
    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, loading = true)
            
            try {
                val response = repository.login(uiState.typedCredentials)
                setAuthTokens(response.tokens)

                uiState = uiState.copy(loggedIn = true)
            } catch (_: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    /**
     * Sets the typed email.
     */
    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    /**
     * Sets the typed password.
     */
    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    /**
     * Dismisses the error dialog.
     */
    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }

    /**
     * Resets the logged in state.
     */
    fun resetLoggedIn() {
        uiState = uiState.copy(loggedIn = false)
    }
}