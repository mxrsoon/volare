package com.mxrsoon.volare.user

import org.jetbrains.exposed.dao.id.UUIDTable

/**
 * Users database table.
 */
object Users : UUIDTable() {
    val firstName = varchar("firstName", length = 30)
    val lastName = varchar("lastName", length = 30)
    val email = varchar("email", length = 254)
    val password = varchar("password", length = 60)
}