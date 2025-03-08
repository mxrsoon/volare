package com.mxrsoon.volare.plugins

import com.mxrsoon.volare.auth.authRoutes
import com.mxrsoon.volare.collection.collectionRoutes
import com.mxrsoon.volare.item.itemRoutes
import com.mxrsoon.volare.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

/**
 * Configures call routing.
 */
fun Application.configureRouting() {
    routing {
        authRoutes()
        userRoutes()
        collectionRoutes()
        itemRoutes()
    }
}
