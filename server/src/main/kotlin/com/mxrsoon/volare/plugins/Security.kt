package com.mxrsoon.volare.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mxrsoon.volare.auth.jwtParams
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

lateinit var jwtVerifier: JWTVerifier
    private set

fun Application.configureSecurity() {
    val jwtParams = jwtParams

    jwtVerifier = JWT
        .require(Algorithm.HMAC256(jwtParams.secret))
        .withAudience(jwtParams.audience)
        .withIssuer(jwtParams.issuer)
        .build()

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
