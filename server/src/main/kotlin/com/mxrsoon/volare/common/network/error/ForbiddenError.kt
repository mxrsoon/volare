package com.mxrsoon.volare.common.network.error

import io.ktor.http.HttpStatusCode

class ForbiddenError(message: String) : HttpError(HttpStatusCode.Forbidden, message)