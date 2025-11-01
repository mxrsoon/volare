package com.mxrsoon.volare.login

import com.mxrsoon.volare.auth.Credentials
import kotlinx.serialization.Serializable

/**
 * Route for the login screen.
 */
@Serializable
data class LoginRoute(
    val email: String? = null,
    val password: String? = null
) {
    companion object {

        /**
         * Creates a [LoginRoute] from the given [Credentials].
         */
        fun from(credentials: Credentials?) = LoginRoute(
            email = credentials?.email,
            password = credentials?.password
        )
    }
}