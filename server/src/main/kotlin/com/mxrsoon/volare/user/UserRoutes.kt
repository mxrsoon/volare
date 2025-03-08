package com.mxrsoon.volare.user

import com.mxrsoon.volare.common.koin.inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.userRoutes() = route("users") {

    val service by inject<UserService>()

    authenticate {
        get("{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Invalid ID")
            val user = service.get(id)

            call.respond(HttpStatusCode.OK, user)
        }
    }
}