package com.mxrsoon.volare

import com.mxrsoon.volare.plugins.configureDatabase
import com.mxrsoon.volare.plugins.configureDependencyInjection
import com.mxrsoon.volare.plugins.configureErrorHandling
import com.mxrsoon.volare.plugins.configureMonitoring
import com.mxrsoon.volare.plugins.configureRouting
import com.mxrsoon.volare.plugins.configureAuthentication
import com.mxrsoon.volare.plugins.configureCors
import com.mxrsoon.volare.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureDatabase()
    configureMonitoring()
    configureAuthentication()
    configureErrorHandling()
    configureCors()
    configureRouting()
}
