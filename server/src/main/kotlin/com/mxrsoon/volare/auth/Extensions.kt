package com.mxrsoon.volare.auth

import com.auth0.jwt.interfaces.DecodedJWT
import com.mxrsoon.volare.auth.refresh.JwtClaims
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

/**
 * The ID of the authenticated user.
 */
val ApplicationCall.authenticatedUserId: String
    get() = principal<JWTPrincipal>()?.payload?.getClaim(JwtClaims.USER_ID)?.asString()
        ?: throw IllegalStateException("The user is not authenticated")

/**
 * The user ID stored in the JWT.
 */
val DecodedJWT.userId: String?
    get() = claims?.get(JwtClaims.USER_ID)?.asString()