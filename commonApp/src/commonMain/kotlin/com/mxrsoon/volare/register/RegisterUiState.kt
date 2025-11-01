package com.mxrsoon.volare.register

import com.mxrsoon.volare.auth.Credentials

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val passwordMismatch: Boolean = false,
    val showError: Boolean = false,
    val loading: Boolean = false,
    val registeredCredentials: Credentials? = null
)