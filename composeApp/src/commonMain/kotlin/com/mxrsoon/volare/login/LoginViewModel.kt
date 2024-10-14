package com.mxrsoon.volare.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mxrsoon.volare.common.network.setAuthTokens
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(LoginUiState())
        private set
    
    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, loading = true)
            
            try {
                val response = repository.login(uiState.email, uiState.password)
                setAuthTokens(response.tokens)

                uiState = uiState.copy(loggedIn = true)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
    
    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }
    
    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }
}