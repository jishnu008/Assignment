package com.android.assignment

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.assignment.ui.adduser.AddUserScreen
import com.android.assignment.ui.moviedetail.MovieDetailScreen
import com.android.assignment.ui.movielist.MovieListScreen
import com.android.assignment.ui.userlist.UserListScreen
import dagger.hilt.android.AndroidEntryPoint

object AppScreens {
    const val UserListRoute = "userList"
    const val MovieListRoute = "movieList"
    const val MovieDetailRoute = "movieDetail"
    const val AddUserRoute = "addUser"

    object UserList { val route = UserListRoute }
    object MovieList { val route = MovieListRoute }
    object MovieDetail { val route = MovieDetailRoute }
    object AddUser { val route = AddUserRoute }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UserListScreen(navController = navController)
        }
        composable(
            "movieList/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            MovieListScreen(navController = navController, userId = userId, viewModel = hiltViewModel())
        }
        composable(
            "movieDetail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailScreen(navController = navController, movieId = movieId, viewModel = hiltViewModel())
        }

    }
}

@Composable
fun MainNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.UserList.route) {
        composable(route = AppScreens.UserList.route) {
            UserListScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable(
            route = "${AppScreens.MovieList.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            MovieListScreen(
                navController = navController,
                userId = userId,
                viewModel = hiltViewModel()
            )
        }
        composable(route = AppScreens.AddUser.route) {
            AddUserScreen(navController = navController)
        }
        composable(
            route = "${AppScreens.MovieDetail.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry: NavBackStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailScreen(
                navController = navController,
                movieId = movieId,
                viewModel = hiltViewModel()
            )
        }
    }
}