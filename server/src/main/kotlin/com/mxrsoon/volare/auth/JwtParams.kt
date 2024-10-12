package com.mxrsoon.volare.auth

import io.ktor.server.application.Application

/**
 * Represents parameters for configuring JWT.
 */
data class JwtParams(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
)