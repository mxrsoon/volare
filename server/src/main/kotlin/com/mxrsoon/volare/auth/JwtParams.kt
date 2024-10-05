package com.mxrsoon.volare.auth

import io.ktor.server.application.Application

/**
 * Represents parameters for configuring JWT.
 */
data class JwtParams(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)

/**
 * Returns [JwtParams] configured for this application.
 */
val Application.jwtParams: JwtParams
    get() = JwtParams(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )