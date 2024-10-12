package com.mxrsoon.volare.user

import com.mxrsoon.volare.common.database.dbQuery
import java.util.UUID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.updateReturning

/**
 * Repository for managing users.
 */
class UserRepository {

    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    suspend fun create(user: User): User = dbQuery {
        val result = Users.insert {
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[email] = user.email
            it[password] = user.password
        }

        result.resultedValues?.singleOrNull()?.toUser()
            ?: throw IllegalStateException("Unable to create user")
    }

    suspend fun list(): List<User> = dbQuery {
        Users.selectAll().map { it.toUser() }
    }

    suspend fun findById(id: String): User? = dbQuery {
        Users.selectAll()
            .where { Users.id eq UUID.fromString(id) }
            .map { it.toUser() }
            .singleOrNull()
    }

    suspend fun findByEmail(email: String): User? = dbQuery {
        Users.selectAll()
            .where { Users.email eq email }
            .map { it.toUser() }
            .singleOrNull()
    }

    suspend fun update(id: String, user: User): User = dbQuery {
        val result = Users.updateReturning(where = { Users.id eq UUID.fromString(id) }) {
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[email] = user.email
            it[password] = user.password
        }.single()

        result.toUser()
    }

    suspend fun delete(id: String) {
        dbQuery {
            Users.deleteWhere { Users.id eq UUID.fromString(id) }
        }
    }
}