package com.mxrsoon.volare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mxrsoon.volare.auth.google.GoogleAuthManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val authManager by lazy { GoogleAuthManager(this) }
    private var googleAuthToken: String? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }

            App(
                enableGoogleSignIn = true,
                googleAuthToken = googleAuthToken,
                onGoogleSignInRequest = { onGoogleSignInRequest() },
                onGoogleSignOutRequest = { onGoogleSignOutRequest() }
            )
        }
    }

    private fun onGoogleSignInRequest() {
        lifecycleScope.launch {
            googleAuthToken = authManager.signIn()
        }
    }

    private fun onGoogleSignOutRequest() {
        lifecycleScope.launch {
            googleAuthToken = null
            authManager.clearCredentials()
        }
    }
}