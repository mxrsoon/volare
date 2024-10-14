package com.mxrsoon.volare.common.network.error

import io.ktor.http.HttpStatusCode

abstract class HttpError(val statusCode: HttpStatusCode, override val message: String) : Error(message)