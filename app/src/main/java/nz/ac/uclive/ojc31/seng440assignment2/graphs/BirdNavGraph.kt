package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.composable
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdDetailsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdListScreen

fun NavGraphBuilder.birdNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    navigation(startDestination = SubScreen.BirdList.route, route = Screen.Birds.route) {

        composable(route = SubScreen.BirdList.route) {
            Box(Modifier.padding(innerPadding)) {
                BirdListScreen(navController = navController)
            }
        }

        composable(
            SubScreen.BirdDetails.route, arguments = listOf(
                navArgument(SubScreen.BirdDetails.birdId) { type = NavType.StringType },
                navArgument(SubScreen.BirdDetails.birdName) { type = NavType.StringType }
        )
        ) {
            val birdId = remember { it.arguments?.getString(SubScreen.BirdDetails.birdId) }
            val birdName = remember { it.arguments?.getString(SubScreen.BirdDetails.birdName) }

            SwipeToReturn(navController = navController) {
                BirdDetailsScreen(
                    birdId = birdId ?: "",
                    birdName = birdName ?: "",
                    navController = navController,
                )
            }
        }
    }
}