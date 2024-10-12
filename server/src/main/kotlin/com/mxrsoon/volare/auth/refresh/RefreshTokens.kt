package com.mxrsoon.volare.auth.refresh

import com.mxrsoon.volare.user.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

/**
 * Refresh tokens database table.
 */
object RefreshTokens : Table() {
    val token = varchar("token", length = 1000).uniqueIndex()
    val userId = reference("userId", Users, onDelete = ReferenceOption.CASCADE)
    val expiresAt = varchar("expiresAt", length = 50)

    override val primaryKey: PrimaryKey = PrimaryKey(token)
}