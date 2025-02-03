package com.mxrsoon.volare.user

import kotlinx.serialization.Serializable

@Serializable
data class UserReference(
    val id: String,
    val firstName: String,
    val lastName: String?
)
