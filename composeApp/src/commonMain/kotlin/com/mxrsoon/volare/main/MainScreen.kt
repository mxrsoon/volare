package com.mxrsoon.volare.main

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    Scaffold(
        modifier = Modifier.imePadding(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                TopLevelRoutes.entries.forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(route.icon), contentDescription = null) },
                        label = { Text(text = stringResource(route.label)) },
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true,
                        onClick = {
                            navController.navigate(route.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true

                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> { HomeScreen(onSignOut = onSignOut) }
            composable<SearchRoute> { SearchScreen() }
            composable<CollectionsRoute> { CollectionsScreen() }
        }
    }
}