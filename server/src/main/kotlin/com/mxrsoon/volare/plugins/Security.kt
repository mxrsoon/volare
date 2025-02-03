package com.mxrsoon.volare.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mxrsoon.volare.auth.GoogleAuthParams
import com.mxrsoon.volare.auth.JwtParams
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.inject

/**
 * Configures application security.
 */
fun Application.configureSecurity() {
    val jwtParams by inject<JwtParams>()
    val jwtVerifier by inject<JWTVerifier>()

    authentication {
        jwt {
            realm = jwtParams.realm
            verifier(jwtVerifier)

            validate { credential ->
                if (credential.payload.audience.contains(jwtParams.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

/**
 * Loads JWT parameters from the environment.
 */
fun Application.loadJwtParams() =
    JwtParams(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        realm = environment.config.property("jwt.realm").getString()
    )

/**
 * Loads JWT parameters from the environment.
 */
fun Application.loadGoogleParams() =
    GoogleAuthParams(
        clientId = environment.config.property("oauth.google.client-id").getString(),
        secret = environment.config.property("oauth.google.secret").getString()
    )

/**
 * Creates a JWT verifier for the application.
 */
fun createJwtVerifier(jwtParams: JwtParams): JWTVerifier = JWT
    .require(Algorithm.HMAC256(jwtParams.secret))
    .withAudience(jwtParams.audience)
    .withIssuer(jwtParams.issuer)
    .build()