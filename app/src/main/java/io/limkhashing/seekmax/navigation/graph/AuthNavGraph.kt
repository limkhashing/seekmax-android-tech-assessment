package io.limkhashing.seekmax.navigation.graph

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.presentation.login.LoginScreen
import io.limkhashing.seekmax.presentation.login.LoginViewModel

fun NavGraphBuilder.authNavGraph(rootNavHostController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.loginState.collectAsStateWithLifecycle(initialValue = ViewState.Idle)
            LoginScreen(
                onLoginSuccess = {
                    viewModel.setJwtSession(state.getSuccessData())
                    rootNavHostController.popBackStack()
                    rootNavHostController.navigate(Graph.MAIN)
                },
                onUserLogin = { username, password ->
                    viewModel.onUserLogin(username, password)
                },
                state = state
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen(route = "login")
}