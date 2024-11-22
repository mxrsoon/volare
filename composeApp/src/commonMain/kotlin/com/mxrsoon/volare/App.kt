package com.mxrsoon.volare

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mxrsoon.volare.common.network.clearAuthTokens
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.login.LoginRoute
import com.mxrsoon.volare.login.LoginScreen
import com.mxrsoon.volare.main.MainRoute
import com.mxrsoon.volare.main.MainScreen
import com.mxrsoon.volare.register.RegisterRoute
import com.mxrsoon.volare.register.RegisterScreen

@Composable
fun App() {
    VolareTheme {
        Box(Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginRoute(),
                enterTransition = { slideIntoContainer(SlideDirection.Start) },
                exitTransition = { slideOutOfContainer(SlideDirection.Start) },
                popEnterTransition = { slideIntoContainer(SlideDirection.End) },
                popExitTransition = { slideOutOfContainer(SlideDirection.End) }
            ) {
                composable<LoginRoute> { entry ->
                    val route = entry.toRoute<LoginRoute>()

                    LoginScreen(
                        presetEmail = route.email,
                        presetPassword = route.password,
                        onSignIn = {
                            navController.navigate(MainRoute) {
                                popUpTo<LoginRoute> {
                                    inclusive = true
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
                                popUpTo<RegisterRoute> {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable<MainRoute> {
                    MainScreen(
                        onSignOut = {
                            clearAuthTokens()

                            navController.navigate(LoginRoute()) {
                                popUpTo<MainRoute> {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
