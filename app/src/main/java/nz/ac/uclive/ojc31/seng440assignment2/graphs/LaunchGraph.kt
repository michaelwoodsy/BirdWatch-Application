package nz.ac.uclive.ojc31.seng440assignment2.graphs

import android.Manifest
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import nz.ac.uclive.ojc31.seng440assignment2.screens.OnboardingScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.SplashScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
fun NavGraphBuilder.launchGraph(navController: NavHostController) {
    composable(route = Screen.Splash.route) {
        SplashScreen(navController = navController)
    }
    composable(route = Screen.Onboarding.route) {
        OnboardingScreen(
            navController = navController,
            permissions = listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        )
    }
}