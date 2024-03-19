package io.limkhashing.seekmax.navigation.graph

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import io.limkhashing.seekmax.presentation.ViewState
import io.limkhashing.seekmax.presentation.main.home.HomeScreen
import io.limkhashing.seekmax.presentation.main.home.HomeViewModel
import io.limkhashing.seekmax.presentation.main.home.jobdetail.JobDetailScreen
import io.limkhashing.seekmax.presentation.main.home.jobdetail.JobDetailViewModel
import io.limkhashing.seekmax.presentation.main.myapplication.MyApplicationScreen
import io.limkhashing.seekmax.presentation.main.profile.ProfileScreen
import io.limkhashing.seekmax.presentation.main.profile.ProfileViewModel
import io.limkhashing.seekmax.presentation.main.profile.editprofile.EditProfileScreen
import io.limkhashing.seekmax.presentation.main.profile.editprofile.EditProfileViewModel

const val JOB_ID_ARGUMENTS = "jobId"
const val IS_JOB_APPLIED_ARGUMENTS = "isJobApplied"

@Composable
fun MainNavGraph(
    mainNavController: NavHostController = rememberNavController(),
    rootNavHostController: NavHostController
) {
    NavHost(
        navController = mainNavController,
        startDestination = BottomBarRoute.Home.route,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween()) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween()) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween()) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween()) }
    ) {

        composable(route = BottomBarRoute.Home.route) { entry ->
            val viewModel = hiltViewModel<HomeViewModel>()
            val publishedJobs = viewModel.publishedJobs.collectAsLazyPagingItems()
            val isJobApplied = entry.savedStateHandle.get<Boolean>(IS_JOB_APPLIED_ARGUMENTS) ?: false
            HomeScreen(
                isJobApplied = isJobApplied,
                publishedJobs = publishedJobs,
                onJobItemCardClicked = { jobId ->
                    mainNavController.navigate(JobDetailsRoute.JobDetails.passJobId(jobId))
                }
            )
        }

        composable(route = BottomBarRoute.MyApplication.route) {
            MyApplicationScreen(name = BottomBarRoute.MyApplication.title, onClick = {
                mainNavController.popBackStack()
            })
        }

        composable(route = BottomBarRoute.Profile.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                onLogoutClicked = {
                    viewModel.logout()
                    rootNavHostController.navigate(AuthScreen.Login.route) {
                        popUpTo(Graph.ROOT) { inclusive = true }
                    }
                },
                onEditProfileClicked = {
                    mainNavController.navigate(EditProfileRoute.EditProfile.route)
                }
            )
        }

        composable(
            route = JobDetailsRoute.JobDetails.route,
            arguments = listOf(
                navArgument(JOB_ID_ARGUMENTS) {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""
                }
            )
        ) {
            var isJobApplied  by remember { mutableStateOf(false) }
            val viewModel = hiltViewModel<JobDetailViewModel>()
            val jobDetailState by viewModel.jobDetailState.collectAsStateWithLifecycle()
            val applyJobState by viewModel.applyJobState.collectAsStateWithLifecycle()
            JobDetailScreen(
                jobDetailState = jobDetailState,
                applyJobState = applyJobState,
                onJobApplied = { applied ->
                    viewModel.fetchJobDetail(viewModel.jobId)
                    mainNavController.previousBackStackEntry?.savedStateHandle?.set(IS_JOB_APPLIED_ARGUMENTS, applied)
                    isJobApplied = applied ?: false
                }
            )
            BackHandler {
                mainNavController.previousBackStackEntry?.savedStateHandle?.set(IS_JOB_APPLIED_ARGUMENTS, isJobApplied)
                mainNavController.popBackStack()
            }
        }

        composable(
            route = EditProfileRoute.EditProfile.route,
        ) {
            val viewModel = hiltViewModel<EditProfileViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle(initialValue = ViewState.Idle)
            EditProfileScreen(
                state = state,
                onChangePasswordClicked = { password ->
                    viewModel.changePassword(password)
                },
                onChangePasswordSuccess = {
                    mainNavController.popBackStack()
                }
            )
        }
    }
}

sealed class BottomBarRoute(
    val route: String,
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Home : BottomBarRoute(
        route = "home",
        title = "Home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
    )

    data object MyApplication : BottomBarRoute(
        route = "application",
        title = "My Application",
        unselectedIcon = Icons.Outlined.AssignmentTurnedIn,
        selectedIcon = Icons.Filled.AssignmentTurnedIn,
    )

    data object Profile : BottomBarRoute(
        route = "profile",
        title = "Profile",
        unselectedIcon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person,
    )
}

sealed class JobDetailsRoute(val route: String) {
    data object JobDetails : JobDetailsRoute(route = "job_details/{$JOB_ID_ARGUMENTS}") {
        fun passJobId(jobId: String) = this.route.replace("{$JOB_ID_ARGUMENTS}", jobId)
    }
}

sealed class EditProfileRoute(val route: String) {
    data object EditProfile : EditProfileRoute(route = "edit_profile")
}