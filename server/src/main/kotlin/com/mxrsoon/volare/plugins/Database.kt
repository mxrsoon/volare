package com.mxrsoon.volare.plugins

import io.ktor.server.application.Application
import kotlin.uuid.ExperimentalUuidApi
import org.jetbrains.exposed.sql.Database

/**
 * Configures the database connection.
 */
fun Application.configureDatabase() {
    Database.connect(
        url = environment.config.property("db.url").getString(),
        user = environment.config.property("db.user").getString(),
        password = environment.config.property("db.password").getString()
    )
}
