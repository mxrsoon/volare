package com.mxrsoon.volare.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import org.slf4j.event.Level

/**
 * Configures application monitoring.
 */
fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        callIdMdc("call-id")
        filter { call -> call.request.path().startsWith("/") }
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId -> callId.isNotEmpty() }
    }
}
