package com.mxrsoon.volare.login

import com.mxrsoon.volare.auth.Credentials

/**
 * Login screen state.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val loading: Boolean = false,
    val loggedIn: Boolean = false
)

/**
 * The currently entered credentials.
 */
val LoginUiState.typedCredentials
    get() = Credentials(email, password)