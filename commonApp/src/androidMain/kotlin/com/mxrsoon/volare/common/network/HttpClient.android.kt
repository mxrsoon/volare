package com.mxrsoon.volare.common.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual val httpClientEngine: HttpClientEngineFactory<HttpClientEngineConfig>
    get() = CIO