package com.mxrsoon.volare.collection

import com.mxrsoon.volare.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * Collections database table.
 */
object Collections : UUIDTable() {
    val name = varchar("name", length = 30)
    val creatorId = reference("userId", Users, onDelete = ReferenceOption.CASCADE)
}