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
            val entries = service.getAll(call.authenticatedUserId)
            call.respond(HttpStatusCode.OK, entries)
        }

        get("{id}") {
            val collection = service.get(
                id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID"),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.OK, collection)
        }

        post {
            val collection = service.create(
                request = call.receive<CreateCollectionRequest>(),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.Created, collection)
        }

        delete("{id}") {
            service.delete(
                id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID"),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.NoContent)
        }
    }
}