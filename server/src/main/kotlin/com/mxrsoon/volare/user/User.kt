package com.mxrsoon.volare.user

import org.jetbrains.exposed.v1.core.ResultRow

/**
 * User model with all fields. Should not be used in API responses, as it contains sensitive data.
 */
data class User(
    val id: String = "",
    val firstName: String,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val googleId: String?
)

/**
 * Converts this user to a reference model that is safe to expose in API responses.
 */
fun User.toReference() = UserReference(
    id = id,
    firstName = firstName,
    lastName = lastName
)

/**
 * Converts this result row to a user.
 */
fun ResultRow.toUser() = User(
    id = this[Users.id].toString(),
    firstName = this[Users.firstName],
    lastName = this[Users.lastName],
    email = this[Users.email],
    password = this[Users.password],
    googleId = this[Users.googleId]
)