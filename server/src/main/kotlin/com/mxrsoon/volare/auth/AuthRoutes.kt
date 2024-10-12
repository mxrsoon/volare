package com.mxrsoon.volare.auth

import com.mxrsoon.volare.common.koin.inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoutes() {

    val service by inject<AuthService>()

    post("login") {
        val response = service.login(credentials = call.receive<Credentials>())
        call.respond(HttpStatusCode.OK, response)
    }

    post("register") {
        val response = service.register(request = call.receive<RegisterRequest>())
        call.respond(HttpStatusCode.Created, response)
    }
}