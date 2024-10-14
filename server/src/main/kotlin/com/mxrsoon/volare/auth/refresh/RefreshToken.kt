package com.mxrsoon.volare.auth.refresh

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class RefreshToken(
    val token: String,
    val userId: String,
    val expiresAt: Instant
)

/**
 * Converts this result row to a user.
 */
fun ResultRow.toRefreshToken() = RefreshToken(
    token = this[RefreshTokens.token],
    userId = this[RefreshTokens.userId].toString(),
    expiresAt = Instant.parse(this[RefreshTokens.expiresAt])
)