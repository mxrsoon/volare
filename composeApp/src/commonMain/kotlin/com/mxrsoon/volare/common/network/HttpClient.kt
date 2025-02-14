package com.mxrsoon.volare.common.network

import com.mxrsoon.volare.auth.TokenPair
import com.mxrsoon.volare.auth.refresh.RefreshTokensRequest
import com.mxrsoon.volare.auth.toBearerTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger as KermitLogger

private const val API_BASE_URL = "http://34.169.52.114:80/"
private var authTokens: TokenPair? = null

/**
 * Get an already configured HttpClient.
 */
val configuredHttpClient: HttpClient by lazy {
    HttpClient(CIO) {
        expectSuccess = true

        install(DefaultRequest) {
            url(API_BASE_URL)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
        
        install(Auth) {
            bearer {
                loadTokens {
                    authTokens?.toBearerTokens()
                }

                refreshTokens {
                    oldTokens?.let {
                        authTokens = client.post("refresh_tokens") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokensRequest(it.refreshToken.orEmpty()))
                        }.body<TokenPair>()

                        authTokens?.toBearerTokens()
                    }
                }
            }
        }

        install(Logging) {
            logger = createLogger()
            level = LogLevel.BODY
        }
    }
}

private fun invalidateBearerTokens() {
    configuredHttpClient.authProviders
        .filterIsInstance<BearerAuthProvider>()
        .singleOrNull()
        ?.clearToken()
}

/**
 * Clear the auth tokens.
 */
fun clearAuthTokens() {
    authTokens = null
    invalidateBearerTokens()
}

/**
 * Set the auth tokens.
 */
fun setAuthTokens(tokens: TokenPair) {
    authTokens = tokens
    invalidateBearerTokens()
}

private fun createLogger(): Logger {
    val logger = KermitLogger.withTag("HttpClient")

    return object : Logger {
        override fun log(message: String) {
            logger.v(message)
        }
    }
}