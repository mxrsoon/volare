package com.mxrsoon.volare

import com.mxrsoon.volare.plugins.configureDatabases
import com.mxrsoon.volare.plugins.configureMonitoring
import com.mxrsoon.volare.plugins.configureRouting
import com.mxrsoon.volare.plugins.configureSecurity
import com.mxrsoon.volare.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
