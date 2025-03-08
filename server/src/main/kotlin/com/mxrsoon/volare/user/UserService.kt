package com.mxrsoon.volare.user

import io.ktor.server.plugins.NotFoundException

/**
 * Service for managing users.
 */
class UserService(
    private val repository: UserRepository
) {

    /**
     * Gets a user by ID.
     */
    suspend fun get(id: String): UserReference =
        repository.findById(id)?.toReference() ?: throw NotFoundException("User not found")
}