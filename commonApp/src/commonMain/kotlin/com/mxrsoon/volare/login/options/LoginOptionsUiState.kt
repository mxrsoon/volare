package com.mxrsoon.volare.login.options

/**
 * Login options screen state.
 */
data class LoginOptionsUiState(
    val showError: Boolean = false,
    val enableOptions: Boolean = true,
    val signingInWithGoogle: Boolean = false,
    val loggedIn: Boolean = false
)