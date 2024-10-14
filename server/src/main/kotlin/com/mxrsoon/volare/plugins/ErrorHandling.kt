package com.mxrsoon.volare.plugins

import com.mxrsoon.volare.common.network.error.HttpError
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<HttpError> { call, cause -> call.respond(cause.statusCode, cause.message) }
    }
}