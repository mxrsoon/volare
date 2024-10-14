package com.mxrsoon.volare.common.network.error

import io.ktor.http.HttpStatusCode

class UnauthorizedError(message: String) : HttpError(HttpStatusCode.Unauthorized, message)