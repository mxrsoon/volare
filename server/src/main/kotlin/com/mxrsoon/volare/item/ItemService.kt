package com.mxrsoon.volare.item

import com.mxrsoon.volare.collection.CollectionRepository
import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.Clock

/**
 * Service for managing items.
 */
class ItemService(
    private val repository: ItemRepository,
    private val collectionRepository: CollectionRepository
) {

    /**
     * Retrieves an item by its ID.
     * @throws NotFoundException If no item with the specified ID is found.
     */
    suspend fun get(id: String, loggedUserId: String): Item {
        val item = repository.findById(id)

        if (!canAccessCollection(item?.collectionId, loggedUserId)) {
            throw NotFoundException("Item not found")
        }

        return item!!
    }

    /**
     * Retrieves all items in a collection.
     */
    suspend fun getAll(collectionId: String, loggedUserId: String): List<Item> {
        if (!canAccessCollection(collectionId, loggedUserId)) {
            throw NotFoundException("Collection not found")
        }

        return repository.findByCollectionId(collectionId)
    }

    /**
     * Creates a new item.
     */
    suspend fun create(request: CreateItemRequest, loggedUserId: String): Item {
        if (!canAccessCollection(request.collectionId, loggedUserId)) {
            throw NotFoundException("Collection not found")
        }

        return repository.create(
            Item(
                name = request.name,
                creatorId = loggedUserId,
                collectionId = request.collectionId,
                createdAt = Clock.System.now(),
                url = request.url
            )
        )
    }

    /**
     * Deletes an item by its ID.
     */
    suspend fun delete(id: String, loggedUserId: String) {
        val item = repository.findById(id)

        if (!canAccessCollection(item?.collectionId, loggedUserId)) {
            throw NotFoundException("Item not found")
        }

        repository.deleteById(id)
    }

    private suspend fun canAccessCollection(collectionId: String?, loggedUserId: String): Boolean {
        val collection = collectionId?.let { collectionRepository.findById(it) }
        return collection != null && collection.creatorId == loggedUserId
    }
}