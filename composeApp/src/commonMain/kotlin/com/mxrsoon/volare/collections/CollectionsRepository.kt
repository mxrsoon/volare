package com.mxrsoon.volare.collections

import com.mxrsoon.volare.collection.Collection
import com.mxrsoon.volare.collection.CreateCollectionRequest
import com.mxrsoon.volare.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CollectionsRepository(private val client: HttpClient = configuredHttpClient) {
    
    /**
     * Retrieves all collections available to the current user.
     */
    suspend fun getAll(): List<Collection> {
        val response = client.get("collections")
        return response.body()
    }

    /**
     * Creates a new collection with the given name.
     */
    suspend fun create(name: String): Collection {
        val response = client.post("collections") {
            contentType(ContentType.Application.Json)
            setBody(CreateCollectionRequest(name))
        }

        return response.body()
    }
}