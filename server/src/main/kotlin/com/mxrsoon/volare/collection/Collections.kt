package com.mxrsoon.volare.collection

import com.mxrsoon.volare.user.Users
import kotlin.time.ExperimentalTime
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.timestamp

/**
 * Collections database table.
 */
@OptIn(ExperimentalTime::class)
object Collections : UUIDTable() {
    val name = varchar("name", length = 254)
    val creatorId = reference("userId", Users, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("createdAt")
}