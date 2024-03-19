package io.limkhashing.seekmax.navigation.graph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.limkhashing.seekmax.presentation.login.LoginViewModel
import io.limkhashing.seekmax.presentation.main.MainScreen

@Composable
fun RootNavigationGraph(rootNavHostController: NavHostController) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val startDestinationGraph = if (loginViewModel.getJwtSession().isNullOrBlank()) {
        Graph.AUTHENTICATION
    } else {
        Graph.MAIN
    }
    NavHost(
        navController = rootNavHostController,
        route = Graph.ROOT,
        startDestination = startDestinationGraph
    ) {
        authNavGraph(rootNavHostController = rootNavHostController)
        composable(route = Graph.MAIN) {
            val mainNavHostController = rememberNavController()
            MainScreen(
                mainNavController = mainNavHostController,
                rootNavHostController = rootNavHostController
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
}