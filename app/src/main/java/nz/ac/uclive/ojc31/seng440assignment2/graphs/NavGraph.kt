package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.*
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Graph.BIRDS
import nz.ac.uclive.ojc31.seng440assignment2.screens.BirdHistoryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.MapScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.HomeScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.SplashScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdDetailsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val showNavigationBar = rememberSaveable { (mutableStateOf(false))}
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val screens = listOf(
        Screen.Map,
        Screen.Home,
        Screen.BirdList,
        Screen.History
    ) // add more nav screens here
    showNavigationBar.value = navBackStackEntry?.destination?.route in screens.map{ it.route}

    Scaffold(
        bottomBar = {
            NavigationBar(
                navController = navController,
                items = screens,
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
                composable(route = Screen.Map.route) {
                    MapScreen()
                }
                composable(route = Screen.History.route) {
                    BirdHistoryScreen(navController = navController)
                }
                birdNavGraph(navController = navController)
            }
        }
    }
}


fun NavGraphBuilder.birdNavGraph(navController: NavHostController) {
    navigation(
        BIRDS,
        "Testing"
    ) {
        composable(route = Screen.BirdList.route) {
            BirdListScreen(navController = navController)
        }
        composable(
            "bird_details_screen/{birdId}",
            arguments = listOf(
                navArgument("birdId") {
                    type = NavType.StringType
                }
            )
        ) {
            val birdName = remember {
                it.arguments?.getString("birdId")
            }
//                        val dominantColor = remember {
//                            val color = it.arguments?.getInt("dominantColor")
//                            color?.let { Color(it) } ?: Color.White
//                        }
            BirdDetailsScreen(
//                            dominantColor = dominantColor,
                birdId = birdName ?: "",
                navController = navController,
            )
        }
    }
    composable(route = Screen.Map.route) {
        MapScreen()
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
                    icon = { 
                        Icon(
                            painter = painterResource(id = screen.icon), 
                            contentDescription = "Navigation Item"
                        )
                    },
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

object Graph {
    const val BIRDS = "bird_graph"
}