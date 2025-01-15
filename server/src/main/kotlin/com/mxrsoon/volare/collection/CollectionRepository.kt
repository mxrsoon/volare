package com.mxrsoon.volare.collection

import com.mxrsoon.volare.common.database.dbQuery
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.updateReturning
import java.util.UUID

/**
 * Repository for managing collections.
 */
class CollectionRepository {

    init {
        transaction {
            SchemaUtils.create(Collections)
        }
    }

    /**
     * Creates a new collection in the database.
     */
    suspend fun create(collection: Collection): Collection = dbQuery {
        val result = Collections.insert {
            it[name] = collection.name
            it[creatorId] = UUID.fromString(collection.creatorId)
        }

        result.resultedValues?.singleOrNull()?.toCollection()
            ?: throw IllegalStateException("Unable to create collection")
    }

    /**
     * Retrieves a list of all collections from the database.
     */
    suspend fun list(): List<Collection> = dbQuery {
        Collections.selectAll().map { it.toCollection() }
    }

    /**
     * Finds a collection by its ID.
     */
    suspend fun findById(id: String): Collection? = dbQuery {
        Collections.selectAll()
            .where { Collections.id eq UUID.fromString(id) }
            .map { it.toCollection() }
            .singleOrNull()
    }

    /**
     * Finds all collections created by a specific user.
     */
    suspend fun findByCreatorId(creatorId: String): List<Collection> = dbQuery {
        Collections.selectAll()
            .where { Collections.creatorId eq UUID.fromString(creatorId) }
            .map { it.toCollection() }
    }

    /**
     * Updates a collection in the database.
     */
    suspend fun update(id: String, collection: Collection): Collection = dbQuery {
        Collections.updateReturning(where = { Collections.id eq UUID.fromString(id) }) {
            it[name] = collection.name
            it[creatorId] = UUID.fromString(collection.creatorId)
        }.single().toCollection()
    }

    /**
     * Deletes a collection from the database based on its ID.
     */
    suspend fun delete(id: String) {
        dbQuery {
            Collections.deleteWhere { Collections.id eq UUID.fromString(id) }
        }
    }
}