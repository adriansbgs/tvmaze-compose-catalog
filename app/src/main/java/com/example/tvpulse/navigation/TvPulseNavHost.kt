package com.example.tvpulse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tvpulse.ui.main.MainScreen
import com.example.tvpulse.ui.detail.DetailRoute

@Composable
fun TvPulseNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = MainRouteKey) {
        composable<MainRouteKey> {
            MainScreen(onShowClick = { showId -> navController.navigate(DetailRouteKey(id = showId)) })
        }
        composable<DetailRouteKey> {
            DetailRoute(onBackClick = { navController.popBackStack() })
        }
    }
}
