package com.mxrsoon.volare.plugins

import com.mxrsoon.volare.auth.authRoutes
import com.mxrsoon.volare.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Configures call routing.
 */
fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()

        // TODO: Remove
        testRoutes()
    }
}

private fun Route.testRoutes() {
    get("/") {
        call.respondText("Hello World!")
    }

    authenticate {
        get("/secured") {
            call.respondText("Hello Secured World!")
        }
    }
}