package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import nz.ac.uclive.ojc31.seng440assignment2.screens.BirdHistoryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.ViewEntryScreen

fun NavGraphBuilder.historyGraph(navController: NavHostController, innerPadding: PaddingValues) {
    composable(route = Screen.History.route) {
        Box(Modifier.padding(innerPadding)) {
            BirdHistoryScreen(navController = navController)
        }
    }

    composable(route = SubScreen.ViewEntryScreen.route) { navBackStackEntry ->
        val backStackEntry =
            remember(navBackStackEntry) { navController.getBackStackEntry(Screen.History.route) }
        Box(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
        ) {
            Box(Modifier.padding(innerPadding)) {
                SwipeToReturn(navController = navController) {
                    ViewEntryScreen(
                        navController = navController,
                        historyViewModel = hiltViewModel(backStackEntry)
                    )
                }
            }
        }
    }
}