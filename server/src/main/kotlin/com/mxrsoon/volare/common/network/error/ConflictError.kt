package com.mxrsoon.volare.common.network.error

import io.ktor.http.HttpStatusCode

class ConflictError(message: String) : HttpError(HttpStatusCode.Conflict, message)