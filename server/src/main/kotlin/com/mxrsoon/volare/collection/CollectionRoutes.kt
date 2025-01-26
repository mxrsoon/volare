package com.mxrsoon.volare.collection

import com.mxrsoon.volare.auth.authenticatedUserId
import com.mxrsoon.volare.common.koin.inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.collectionRoutes() = route("collections") {

    val service by inject<CollectionService>()

    authenticate {
        get {
            val collections = service.getAll(call.authenticatedUserId)
            call.respond(HttpStatusCode.OK, collections)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val collection = service.get(id, call.authenticatedUserId)

            call.respond(HttpStatusCode.OK, collection)
        }

        post {
            val request = call.receive<CreateCollectionRequest>()
            val collection = service.create(request, call.authenticatedUserId)

            call.respond(HttpStatusCode.Created, collection)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            service.delete(id, call.authenticatedUserId)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}