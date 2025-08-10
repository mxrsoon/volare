package com.mxrsoon.volare.auth.refresh

import com.mxrsoon.volare.user.Users
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

/**
 * Refresh tokens database table.
 */
object RefreshTokens : Table() {
    val token = varchar("token", length = 1000).uniqueIndex()
    val userId = reference("userId", Users, onDelete = ReferenceOption.CASCADE)
    val revoked = bool("expiresAt")

    override val primaryKey: PrimaryKey = PrimaryKey(token)
}