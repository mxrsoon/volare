package com.mxrsoon.volare.plugins

import com.mxrsoon.volare.auth.AuthService
import com.mxrsoon.volare.auth.refresh.RefreshTokenRepository
import com.mxrsoon.volare.collection.CollectionRepository
import com.mxrsoon.volare.collection.CollectionService
import com.mxrsoon.volare.user.UserRepository
import com.mxrsoon.volare.user.UserService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

/**
 * Configures the dependency injection.
 */
fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            module {
                single { loadJwtParams() }
                single { createJwtVerifier(get()) }
                single { UserRepository() }
                single { UserService(get()) }
                single { RefreshTokenRepository() }
                single { AuthService(get(), get(), get(), get()) }
                single { CollectionRepository() }
                single { CollectionService(get()) }
            }
        )
    }
}