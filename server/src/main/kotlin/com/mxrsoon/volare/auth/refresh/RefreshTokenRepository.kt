package com.mxrsoon.volare.auth.refresh

import com.mxrsoon.volare.common.database.dbQuery
import java.util.UUID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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