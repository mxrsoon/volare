package com.mxrsoon.volare.plugins

import com.mxrsoon.volare.auth.authRoutes
import com.mxrsoon.volare.collection.collectionRoutes
import com.mxrsoon.volare.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

/**
 * Configures call routing.
 */
fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()
        collectionRoutes()

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