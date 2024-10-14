package com.mxrsoon.volare.login

import com.mxrsoon.volare.auth.TokenPair
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: String,
    val tokens: TokenPair
)
