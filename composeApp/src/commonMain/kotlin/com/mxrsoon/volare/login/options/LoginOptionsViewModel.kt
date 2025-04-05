package com.mxrsoon.volare.login.options

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mxrsoon.volare.common.network.setAuthTokens
import com.mxrsoon.volare.login.LoginRepository
import kotlinx.coroutines.launch

/**
 * View model for the login options screen.
 */
class LoginOptionsViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {

    /**
     * The current state of the login options screen.
     */
    var uiState by mutableStateOf(LoginOptionsUiState())
        private set

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

    /**
     * Signs in using a Google auth token.
     */
    fun signInWithGoogle(googleAuthToken: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                showError = false,
                signingInWithGoogle = true,
                enableOptions = false
            )

            try {
                val response = repository.signInWithGoogle(googleAuthToken)
                setAuthTokens(response.tokens)

                uiState = uiState.copy(loggedIn = true)
            } catch (_: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(signingInWithGoogle = false, enableOptions = true)
            }
        }
    }
}