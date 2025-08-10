package com.mxrsoon.volare.auth.refresh

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.ResultRow

@Serializable
data class RefreshToken(
    val token: String,
    val userId: String,
    val revoked: Boolean
)

/**
 * Converts this result row to a user.
 */
fun ResultRow.toRefreshToken() = RefreshToken(
    token = this[RefreshTokens.token],
    userId = this[RefreshTokens.userId].toString(),
    revoked = this[RefreshTokens.revoked]
)