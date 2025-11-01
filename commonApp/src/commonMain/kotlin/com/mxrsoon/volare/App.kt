package com.mxrsoon.volare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.mxrsoon.volare.common.network.clearAuthTokens
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.login.LoginRoute
import com.mxrsoon.volare.login.LoginScreen
import com.mxrsoon.volare.login.options.LoginOptionsRoute
import com.mxrsoon.volare.login.options.LoginOptionsScreen
import com.mxrsoon.volare.main.MainRoute
import com.mxrsoon.volare.main.MainScreen
import com.mxrsoon.volare.register.RegisterRoute
import com.mxrsoon.volare.register.RegisterScreen

@Composable
fun App(
    enableGoogleSignIn: Boolean = false,
    googleAuthToken: String? = null,
    onGoogleSignInRequest: () -> Unit = {},
    onGoogleSignOutRequest: () -> Unit = {}
) {
    VolareTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = RootRoute
            ) {
                navigation<RootRoute>(startDestination = LoginOptionsRoute) {
                    composable<LoginOptionsRoute> { entry ->
                        LoginOptionsScreen(
                            enableGoogleSignIn = enableGoogleSignIn,
                            googleAuthToken = googleAuthToken,
                            onGoogleSignInRequest = onGoogleSignInRequest,
                            onEmailSignInClick = {
                                navController.navigate(LoginRoute())
                            },
                            onRegisterClick = {
                                navController.navigate(RegisterRoute())
                            },
                            onSignIn = {
                                navController.navigate(MainRoute) {
                                    popUpTo<RootRoute> {
                                        inclusive = false
                                    }
                                }
                            }
                        )
                    }

                    composable<LoginRoute> { entry ->
                        val route = entry.toRoute<LoginRoute>()

                        LoginScreen(
                            presetEmail = route.email,
                            presetPassword = route.password,
                            onSignIn = {
                                navController.navigate(MainRoute) {
                                    popUpTo<RootRoute> {
                                        inclusive = false
                                    }
                                }
                            },
                            onRegisterClick = { credentials ->
                                navController.navigate(RegisterRoute.from(credentials))
                            }
                        )
                    }

                    composable<RegisterRoute> { entry ->
                        val route = entry.toRoute<LoginRoute>()

                        RegisterScreen(
                            presetEmail = route.email,
                            presetPassword = route.password,
                            onLoginClick = { navController.popBackStack() },
                            onRegistrationComplete = { credentials ->
                                navController.navigate(LoginRoute.from(credentials)) {
                                    popUpTo<RootRoute> {
                                        inclusive = false
                                    }
                                }
                            }
                        )
                    }

                    composable<MainRoute> {
                        MainScreen(
                            onSignOut = {
                                clearAuthTokens()
                                onGoogleSignOutRequest()

                                navController.navigate(LoginRoute()) {
                                    popUpTo<RootRoute> {
                                        inclusive = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
