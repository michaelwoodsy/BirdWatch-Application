package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import nz.ac.uclive.ojc31.seng440assignment2.screens.SplashScreen
import androidx.compose.ui.Modifier
import nz.ac.uclive.ojc31.seng440assignment2.screens.BirdListScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.HomeScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.MapScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val showNavigationBar = rememberSaveable { (mutableStateOf(true))}
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // hide navigation bar on these pages
    when (navBackStackEntry?.destination?.route) {
        Screen.Splash.route -> showNavigationBar.value = false
        else -> showNavigationBar.value = true
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                navController = navController,
                items = listOf(
                    Screen.Map,
                    Screen.Home,
                    Screen.BirdList
                ), // Add more screens here
                showNavigationBar = showNavigationBar
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
                    HomeScreen()
                }
                composable(route = Screen.BirdList.route) {
                    BirdListScreen(navController = navController)
                }
                composable(route = Screen.Map.route) {
                    MapScreen()
                }
            }
        }
    }
}


@Composable
fun NavigationBar(navController: NavHostController, items: List<Screen>, showNavigationBar: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = showNavigationBar.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true,
                    label = {Text(stringResource(screen.description))},
                    icon = { Icon(screen.icon, stringResource(screen.description) ) },
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
}