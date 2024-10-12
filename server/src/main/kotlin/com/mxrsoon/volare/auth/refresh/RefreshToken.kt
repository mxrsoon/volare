package com.mxrsoon.volare.auth.refresh

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val token: String,
    val userId: String,
    val expiresAt: Instant
)
