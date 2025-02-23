package com.mxrsoon.volare.item

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
 * Repository for managing items.
 */
class ItemRepository {

    init {
        transaction {
            SchemaUtils.create(Items)
        }
    }

    /**
     * Creates a new item in the database.
     */
    suspend fun create(item: Item): Item = dbQuery {
        val result = Items.insert {
            it[name] = item.name
            it[creatorId] = UUID.fromString(item.creatorId)
            it[collectionId] = UUID.fromString(item.collectionId)
            it[createdAt] = item.createdAt
            it[url] = item.url
        }

        result.resultedValues?.singleOrNull()?.toItem()
            ?: throw IllegalStateException("Unable to create item")
    }

    /**
     * Finds an item by its ID.
     */
    suspend fun findById(id: String): Item? = dbQuery {
        Items.selectAll()
            .where { Items.id eq UUID.fromString(id) }
            .map { it.toItem() }
            .singleOrNull()
    }

    /**
     * Finds all items in a specific collection.
     */
    suspend fun findByCollectionId(collectionId: String): List<Item> = dbQuery {
        Items.selectAll()
            .where { Items.collectionId eq UUID.fromString(collectionId) }
            .map { it.toItem() }
    }

    /**
     * Updates an item in the database.
     */
    suspend fun update(id: String, item: Item): Item = dbQuery {
        Items.updateReturning(where = { Items.id eq UUID.fromString(id) }) {
            it[name] = item.name
            it[url] = item.url
        }.single().toItem()
    }

    /**
     * Deletes an item from the database based on its ID.
     */
    suspend fun deleteById(id: String) {
        dbQuery {
            Items.deleteWhere { Items.id eq UUID.fromString(id) }
        }
    }

    /**
     * Counts the number of items in a specific collection.
     */
    suspend fun countByCollectionId(collectionId: String): Int = dbQuery {
        Items.selectAll()
            .where { Items.collectionId eq UUID.fromString(collectionId) }
            .count()
            .toInt()
    }
}