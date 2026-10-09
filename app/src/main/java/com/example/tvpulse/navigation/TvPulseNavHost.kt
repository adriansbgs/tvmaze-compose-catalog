package com.example.tvpulse.navigation

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.tvpulse.ui.detail.DetailRoute
import com.example.tvpulse.ui.main.MainScreen

private const val DEEPLINK_DETAIL_BASE = "movieapp://detail"

@Composable
fun TvPulseNavHost(navController: NavHostController = rememberNavController()) {
    // Warm start: singleTop delivers the link to onNewIntent instead of creating a new Activity.
    // Cold start needs nothing extra, NavHost reads the launch intent itself.
    val activity = LocalActivity.current as? ComponentActivity
    DisposableEffect(activity, navController) {
        val listener = Consumer<Intent> { navController.handleDeepLink(it) }
        activity?.addOnNewIntentListener(listener)
        onDispose { activity?.removeOnNewIntentListener(listener) }
    }

    NavHost(navController = navController, startDestination = MainRouteKey) {
        composable<MainRouteKey> {
            MainScreen(onShowClick = { navController.navigate(DetailRouteKey(it)) })
        }
        composable<DetailRouteKey>(
            deepLinks = listOf(navDeepLink<DetailRouteKey>(basePath = DEEPLINK_DETAIL_BASE)),
        ) {
            DetailRoute(onBackClick = { navController.popBackStack() })
        }
    }
}
