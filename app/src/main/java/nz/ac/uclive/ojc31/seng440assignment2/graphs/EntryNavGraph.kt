package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.AddEntryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.CameraScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.LoadEntryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.SelectLocationScreen

fun NavGraphBuilder.entryNavGraph(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    service: WeeklyNotificationService
) {

    composable(
        SubScreen.AddEntryDetails.route, arguments = listOf(
            navArgument(SubScreen.AddEntryDetails.birdId) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SubScreen.AddEntryDetails.birdName) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SubScreen.AddEntryDetails.lat) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SubScreen.AddEntryDetails.long) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SubScreen.AddEntryDetails.challengeId) {
                type = NavType.StringType
                nullable = true
            }
    )
    ) {
        SwipeToReturn(navController = navController) {
            AddEntryScreen(
                navController = navController,
                service = service,
            )
        }
    }


    composable(route = SubScreen.SelectLocationScreen.route) {
        val backStackEntry =
            remember(navBackStackEntry) { navController.getBackStackEntry(SubScreen.AddEntryDetails.route) }
        SelectLocationScreen(
            navController = navController,
            viewModel = hiltViewModel(backStackEntry)
        )
    }

    composable(route = SubScreen.CameraScreen.route) {
        val backStackEntry =
            remember(navBackStackEntry) { navController.getBackStackEntry(SubScreen.AddEntryDetails.route) }

        SwipeToReturn(navController = navController) {
            CameraScreen(navController = navController, viewModel = hiltViewModel(backStackEntry))
        }
    }

    composable(
        route= SubScreen.LoadEntryFromLink.route,
        deepLinks = listOf(navDeepLink { uriPattern = SubScreen.LoadEntryFromLink.deepLinkURL })
    ) { backStackEntry ->
        val birdId = backStackEntry.arguments?.getString(SubScreen.LoadEntryFromLink.birdId)
        val lat = backStackEntry.arguments?.getString(SubScreen.LoadEntryFromLink.lat)?.toDoubleOrNull()
        val long = backStackEntry.arguments?.getString(SubScreen.LoadEntryFromLink.long)?.toDoubleOrNull()

        if (!birdId.isNullOrEmpty() && lat != null && long != null) {
            LoadEntryScreen(
                navController = navController,
                birdId = birdId,
                lat = lat,
                long = long
            )
        } else {
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route)
                    launchSingleTop = true
                }
            }
        }
    }
}