package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import nz.ac.uclive.ojc31.seng440assignment2.screens.SplashScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.HomeScreen
import androidx.compose.ui.Modifier

@Composable
fun NavGraph(navController: NavHostController) {

    Scaffold(
        bottomBar = {
            NavigationBar(
                navController = navController,
                items = listOf(Screen.Home) // Add more screens here
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {

                composable(route = Screen.Splash.route) {
                    SplashScreen(navController = navController)
                }
                composable(route = Screen.Home.route) {
                    HomeScreen(navController = navController)
                }
            }
        }
    }
}


@Composable
fun NavigationBar(navController: NavHostController, items: List<Screen>) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true,
                label = {Text(stringResource(screen.description))},
                icon = { Icon(screen.icon, "" ) },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                })
        }
    }
}