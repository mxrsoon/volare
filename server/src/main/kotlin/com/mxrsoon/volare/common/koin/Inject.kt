package com.mxrsoon.volare.common.koin

import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import org.koin.ktor.ext.inject

/**
 * Temporary workaround for injecting dependencies inside routes until Koin resolves a
 * [compatibility issue](https://github.com/InsertKoinIO/koin/issues/1716) with Ktor 3.x.x.
 */
inline fun <reified T : Any> Route.inject(): Lazy<T> =
    application.inject<T>()