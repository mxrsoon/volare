package com.mxrsoon.volare.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.ClearCredentialStateRequest.Companion.TYPE_CLEAR_CREDENTIAL_STATE
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

class GoogleAuthManager(private val context: Context) {

    private val credentialManager by lazy { CredentialManager.create(context) }

    suspend fun signIn(): String {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            return handleResult(result)
        } catch (error: NoCredentialException) {
            return signUp()
        } catch (error: GetCredentialException) {
            throw IllegalStateException("Unable to authenticate", error)
        }
    }

    private suspend fun signUp(): String {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            return handleResult(result)
        } catch (error: GetCredentialException) {
            throw IllegalStateException("Unable to authenticate", error)
        }
    }

    private fun handleResult(result: GetCredentialResponse): String {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        return googleIdTokenCredential.idToken
                    } catch (error: GoogleIdTokenParsingException) {
                        throw IllegalStateException("Received an invalid google id token response", error)
                    }
                } else {
                    throw IllegalStateException("Unexpected type of credential")
                }
            }

            else -> {
                throw IllegalStateException("Unexpected type of credential")
            }
        }
    }

    suspend fun clearCredentials() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest(TYPE_CLEAR_CREDENTIAL_STATE))
    }

    companion object {
        private const val WEB_CLIENT_ID = "694838305957-2bnolf2brff91veat04lanv0p3rs2sld.apps.googleusercontent.com"
    }
}