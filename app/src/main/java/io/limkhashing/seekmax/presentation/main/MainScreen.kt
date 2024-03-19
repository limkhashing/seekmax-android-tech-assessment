package io.limkhashing.seekmax.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.limkhashing.seekmax.presentation.ui.theme.BackgroundSecondaryColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.ui.unit.sp
import io.limkhashing.seekmax.navigation.graph.BottomBarRoute
import io.limkhashing.seekmax.navigation.graph.MainNavGraph

@Composable
fun MainScreen(
    mainNavController: NavHostController,
    rootNavHostController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().background(BackgroundSecondaryColor),
        bottomBar = { BottomBar(navController = mainNavController) }
    ) { _ ->
        // Kinda dirty to pass rootNavHostController to MainNavGraph, but it's the only way to navigate to other graph
        MainNavGraph(mainNavController = mainNavController, rootNavHostController = rootNavHostController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarRoute.Home,
        BottomBarRoute.MyApplication,
        BottomBarRoute.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomAppBar(
            containerColor = BackgroundSecondaryColor
        ) {
            screens.forEach { screen ->
                AddBottomNavigationItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddBottomNavigationItem(
    screen: BottomBarRoute,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    BottomNavigationItem(
        label = { Text(text = screen.title, fontSize = 12.sp) },
        icon = {
            Icon(
                imageVector = if (isSelected) { screen.selectedIcon } else { screen.unselectedIcon },
                contentDescription = screen.title
            )
        },
        selected = isSelected,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        alwaysShowLabel = true,
        modifier = Modifier.background(BackgroundSecondaryColor)
    )
}