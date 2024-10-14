package com.mxrsoon.volare.login

import com.mxrsoon.volare.auth.Credentials
import kotlinx.serialization.Serializable

@Serializable
data class LoginRoute(
    val email: String? = null,
    val password: String? = null
) {

    companion object {

        fun of(credentials: Credentials?) =
            LoginRoute(
                email = credentials?.email,
                password = credentials?.password
            )
    }
}