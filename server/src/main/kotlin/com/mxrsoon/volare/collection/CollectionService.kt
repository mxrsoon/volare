package com.mxrsoon.volare.collection

import com.mxrsoon.volare.item.ItemRepository
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock

/**
 * Service for managing collections.
 */
class CollectionService(
    private val repository: CollectionRepository,
    private val itemRepository: ItemRepository
) {

    /**
     * Retrieves a collection by its ID.
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
     * Retrieves all collections created by the authenticated user.
     */
    suspend fun getAll(loggedUserId: String): List<CollectionListEntry> =
        repository.findByCreatorId(loggedUserId).map { collection ->
            CollectionListEntry(
                collection = collection,
                itemCount = itemRepository.countByCollectionId(collection.id)
            )
        }

    /**
     * Creates a new collection.
     */
    suspend fun create(request: CreateCollectionRequest, loggedUserId: String): Collection =
        repository.create(
            Collection(
                name = request.name,
                creatorId = loggedUserId,
                createdAt = Clock.System.now()
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