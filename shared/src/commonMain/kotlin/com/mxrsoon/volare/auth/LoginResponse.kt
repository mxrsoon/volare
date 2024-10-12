package com.mxrsoon.volare.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: String,
    val tokens: TokenPair
)