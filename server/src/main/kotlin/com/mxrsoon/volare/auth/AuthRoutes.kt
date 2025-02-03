package com.mxrsoon.volare.auth

import com.mxrsoon.volare.auth.refresh.RefreshTokensRequest
import com.mxrsoon.volare.common.koin.inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail

fun Route.authRoutes() {

    val service by inject<AuthService>()

    post("register") {
        val response = service.register(request = call.receive<RegisterRequest>())
        call.respond(HttpStatusCode.Created, response)
    }

    post("login") {
        val response = service.login(credentials = call.receive<Credentials>())
        call.respond(HttpStatusCode.OK, response)
    }

    post("login/google") {
        val params = call.receiveParameters()
        val response = service.signInWithGoogle(idTokenString = params.getOrFail("credential"))

        call.respond(HttpStatusCode.OK, response)
    }

    post("refresh_tokens") {
        val tokens = service.refreshTokens(request = call.receive<RefreshTokensRequest>())
        call.respond(tokens)
    }
}