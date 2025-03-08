package com.mxrsoon.volare.collection

import com.mxrsoon.volare.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CollectionRepository(private val client: HttpClient = configuredHttpClient) {

    /**
     * Retrieves all collections available to the current user.
     */
    suspend fun getAll(): List<CollectionListEntry> {
        val response = client.get(BASE_PATH)
        return response.body()
    }

    /**
     * Creates a new collection with the given name.
     */
    suspend fun create(name: String): Collection {
        val response = client.post(BASE_PATH) {
            contentType(ContentType.Application.Json)
            setBody(CreateCollectionRequest(name = name))
        }

        return response.body()
    }

    /**
     * Deletes a collection by its ID.
     */
    suspend fun delete(id: String) {
        client.delete("$BASE_PATH/$id")
    }

    companion object {
        private const val BASE_PATH = "collections"
    }
}