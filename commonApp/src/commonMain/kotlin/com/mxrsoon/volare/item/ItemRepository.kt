package com.mxrsoon.volare.item

import com.mxrsoon.volare.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ItemRepository(private val client: HttpClient = configuredHttpClient) {
    
    /**
     * Retrieves all items in a collection.
     */
    suspend fun getAll(collectionId: String): List<Item> {
        val response = client.get(BASE_PATH) {
            parameter(COLLECTION_ID_PARAM, collectionId)
        }

        return response.body()
    }

    /**
     * Creates a new item.
     */
    suspend fun create(request: CreateItemRequest): Item {
        val response = client.post(BASE_PATH) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.body()
    }

    /**
     * Deletes an item by its ID.
     */
    suspend fun delete(id: String) {
        client.delete("$BASE_PATH/$id")
    }

    companion object {
        private const val BASE_PATH = "items"
        private const val COLLECTION_ID_PARAM = "collectionId"
    }
}