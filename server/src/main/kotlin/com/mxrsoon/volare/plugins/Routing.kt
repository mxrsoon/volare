package com.mxrsoon.volare.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authenticate {
            get("/secured") {
                call.respondText("Hello Secured World!")
            }
        }
    }
}
