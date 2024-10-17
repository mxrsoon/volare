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
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.home.HomeRoute
import com.mxrsoon.volare.home.HomeScreen
import com.mxrsoon.volare.login.LoginRoute
import com.mxrsoon.volare.login.LoginScreen

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
                            navController.navigate(HomeRoute) {
                                popUpTo<LoginRoute> {
                                    inclusive = true
                                }
                            }
                        },
                        onRegisterClick = {}
                    )
                }

                composable<HomeRoute> {
                    HomeScreen()
                }
            }
        }
    }
}
