package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import nz.ac.uclive.ojc31.seng440assignment2.screens.SettingsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.AchievementsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.HomeScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.StatisticsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.challenge.ChallengesScreen

fun NavGraphBuilder.homeGraph(navController: NavHostController, innerPadding: PaddingValues) {
    composable(route = Screen.Home.route) {
        Box(Modifier.padding(innerPadding)) {
            HomeScreen(navController = navController)
        }
    }
    composable(route = SubScreen.Settings.route) {
        Box(Modifier.padding(innerPadding)) {
            SettingsScreen(navController = navController)
        }
    }
    composable(route = SubScreen.Challenges.route) {
        Box(Modifier.padding(innerPadding)) {
            SwipeToReturn(navController = navController) {
                ChallengesScreen(navController = navController)
            }
        }
    }
    composable(route = SubScreen.Achievements.route) {
        Box(Modifier.padding(innerPadding)) {
            SwipeToReturn(navController = navController) {
                AchievementsScreen(navController = navController)
            }
        }
    }
    composable(route = SubScreen.Statistics.route) {
        Box(Modifier.padding(innerPadding)) {
            SwipeToReturn(navController = navController) {
                StatisticsScreen(navController = navController)
            }
        }
    }
}