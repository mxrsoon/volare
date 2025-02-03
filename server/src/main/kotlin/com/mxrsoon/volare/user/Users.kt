package com.mxrsoon.volare.user

import org.jetbrains.exposed.dao.id.UUIDTable

/**
 * Users database table.
 */
object Users : UUIDTable() {
    val firstName = varchar("firstName", length = 30)
    val lastName = varchar("lastName", length = 30).nullable()
    val email = varchar("email", length = 254).nullable()
    val password = varchar("password", length = 60).nullable()
    val googleId = varchar("googleId", length = 254).nullable()
}