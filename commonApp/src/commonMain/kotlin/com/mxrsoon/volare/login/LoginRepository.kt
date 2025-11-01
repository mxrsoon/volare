package com.mxrsoon.volare.login

import com.mxrsoon.volare.auth.Credentials
import com.mxrsoon.volare.auth.LoginResponse
import com.mxrsoon.volare.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters

class LoginRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun login(credentials: Credentials): LoginResponse {
        val response = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }
        
        return response.body()
    }

    suspend fun signInWithGoogle(googleAuthToken: String): LoginResponse {
        val response = client.submitForm(
            url = "login/google",
            formParameters = parameters {
                append("credential", googleAuthToken)
            }
        )

        return response.body()
    }
}