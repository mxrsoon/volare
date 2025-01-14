package com.mxrsoon.volare.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mxrsoon.volare.collections.CollectionsRoute
import com.mxrsoon.volare.collections.CollectionsScreen
import com.mxrsoon.volare.home.HomeRoute
import com.mxrsoon.volare.home.HomeScreen
import com.mxrsoon.volare.search.SearchRoute
import com.mxrsoon.volare.search.SearchScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    NavigationSuiteScaffold(
        layoutType = if (isImeVisible) NavigationSuiteType.None else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        navigationSuiteItems = {
            TopLevelRoutes.entries.forEach { route ->
                item(
                    icon = { Icon(painter = painterResource(route.icon), contentDescription = null) },
                    label = { Text(text = stringResource(route.label)) },
                    selected = currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true,
                    onClick = {
                        navController.navigate(route.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            modifier = Modifier.fillMaxSize().imePadding(),
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> { HomeScreen(onSignOut = onSignOut) }
            composable<SearchRoute> { SearchScreen() }
            composable<CollectionsRoute> { CollectionsScreen() }
        }
    }
}