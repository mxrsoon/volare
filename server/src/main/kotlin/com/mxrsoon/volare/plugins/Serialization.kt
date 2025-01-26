package com.mxrsoon.volare.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

/**
 * Configures request and response serialization.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
