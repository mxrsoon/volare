package com.mxrsoon.volare.item

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

fun Route.itemRoutes() = route("items") {

    val service by inject<ItemService>()

    authenticate {
        get {
            val entries = service.getAll(
                collectionId = call.parameters["collectionId"]
                    ?: throw IllegalArgumentException("Invalid collection ID"),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.OK, entries)
        }

        get("{id}") {
            val item = service.get(
                id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID"),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.OK, item)
        }

        post {
            val item = service.create(
                request = call.receive<CreateItemRequest>(),
                loggedUserId = call.authenticatedUserId
            )

            call.respond(HttpStatusCode.Created, item)
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