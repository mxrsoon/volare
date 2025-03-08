package com.mxrsoon.volare.item

import com.mxrsoon.volare.collection.Collections
import com.mxrsoon.volare.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

/**
 * Collections database table.
 */
object Items : UUIDTable() {
    val name = varchar("name", length = 254)
    val creatorId = reference("creatorId", Users, onDelete = ReferenceOption.CASCADE)
    val collectionId = reference("collectionId", Collections, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("createdAt")
    val url = varchar("url", length = 254).nullable()
}