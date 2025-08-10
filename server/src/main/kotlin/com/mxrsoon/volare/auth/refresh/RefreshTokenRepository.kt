package com.mxrsoon.volare.auth.refresh

import com.mxrsoon.volare.common.database.dbQuery
import java.util.UUID
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Repository for managing refresh tokens.
 */
class RefreshTokenRepository {

    init {
        transaction {
            SchemaUtils.create(RefreshTokens)
        }
    }

    suspend fun create(refreshToken: RefreshToken): String = dbQuery {
        val result = RefreshTokens.insert {
            it[token] = refreshToken.token
            it[userId] = UUID.fromString(refreshToken.userId)
            it[revoked] = refreshToken.revoked
        }

        result[RefreshTokens.token]
    }

    suspend fun findByToken(token: String): RefreshToken? = dbQuery {
        RefreshTokens.selectAll()
            .where { RefreshTokens.token eq token }
            .map { it.toRefreshToken() }
            .singleOrNull()
    }

    suspend fun deleteByToken(token: String) {
        dbQuery {
            RefreshTokens.deleteWhere { RefreshTokens.token eq token }
        }
    }
}