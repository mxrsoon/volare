package com.mxrsoon.volare.auth

import io.ktor.client.plugins.auth.providers.BearerTokens

/**
 * Convert the [TokenPair] to a [BearerTokens] object.
 */
fun TokenPair.toBearerTokens(): BearerTokens =
    BearerTokens(accessToken, refreshToken)