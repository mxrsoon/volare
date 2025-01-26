package com.mxrsoon.volare.collection

import io.ktor.server.plugins.NotFoundException

/**
 * Service for managing collections.
 */
class CollectionService(
    private val repository: CollectionRepository
) {

    /**
     * Retrieves a collection by its ID.
     *
     * @throws NotFoundException If no collection with the specified ID is found.
     */
    suspend fun get(id: String, loggedUserId: String): Collection {
        val collection = repository.findById(id)

        if (collection == null || collection.creatorId != loggedUserId) {
            throw NotFoundException("Collection not found")
        }

        return collection
    }

    /**
     * Retrieves all collections created by a specific user.
     */
    suspend fun getAll(loggedUserId: String): List<Collection> =
        repository.findByCreatorId(loggedUserId)

    /**
     * Creates a new collection.
     */
    suspend fun create(request: CreateCollectionRequest, loggedUserId: String): Collection =
        repository.create(
            Collection(
                name = request.name,
                creatorId = loggedUserId
            )
        )

    /**
     * Deletes a collection by its ID.
     */
    suspend fun delete(id: String, loggedUserId: String) {
        val collection = repository.findById(id)

        if (collection == null || collection.creatorId != loggedUserId) {
            throw NotFoundException("Collection not found")
        }

        repository.deleteById(id)
    }
}