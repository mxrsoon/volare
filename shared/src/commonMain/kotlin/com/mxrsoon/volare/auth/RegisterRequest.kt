package com.mxrsoon.volare.auth

import kotlinx.serialization.Serializable

/**
 * Request to create a new user.
 */
@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)