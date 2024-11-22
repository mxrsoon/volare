package com.mxrsoon.volare.register

import com.mxrsoon.volare.auth.Credentials
import kotlinx.serialization.Serializable

/**
 * Route for the register screen.
 */
@Serializable
data class RegisterRoute(
    val email: String? = null,
    val password: String? = null
) {
    companion object {

        /**
         * Creates a [RegisterRoute] from the given [Credentials].
         */
        fun from(credentials: Credentials?) =
            RegisterRoute(
                email = credentials?.email,
                password = credentials?.password
            )
    }
}