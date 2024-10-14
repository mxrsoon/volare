package com.mxrsoon.volare.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val loading: Boolean = false,
    val loggedIn: Boolean = false
)