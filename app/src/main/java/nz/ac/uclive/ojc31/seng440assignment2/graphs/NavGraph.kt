package nz.ac.uclive.ojc31.seng440assignment2.graphs

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import nz.ac.uclive.ojc31.seng440assignment2.screens.BirdHistoryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.MapScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.HomeScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.SplashScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdDetailsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdListScreen
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    val showNavigationBar = rememberSaveable { (mutableStateOf(false))}
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val screens = listOf(
        Screen.Map,
        Screen.Home,
        Screen.Birds,
        Screen.History
    )

    when (destination?.route) {
        Screen.Splash.route -> showNavigationBar.value = false
        SubScreen.BirdDetails.route -> showNavigationBar.value = false
        null -> {}
        else -> showNavigationBar.value = true
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                navController = navController,
                items = screens,
                showNavigationBar = showNavigationBar
            )
        }
    ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {

                composable(route = Screen.Splash.route) {
                    SplashScreen(navController = navController)
                }
                composable(route = Screen.Home.route) {
                    Box(Modifier.padding(innerPadding)) {
                        HomeScreen(
                            permissions = listOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
                composable(route = Screen.Map.route) {
                    Box(Modifier.padding(innerPadding)) {
                        MapScreen()
                    }
                }
                composable(route = Screen.History.route) {
                    Box(Modifier.padding(innerPadding)) {
                        BirdHistoryScreen(navController = navController)
                    }
                }
                birdNavGraph(navController = navController, innerPadding = innerPadding)
            }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToReturn(navController: NavHostController, content : @Composable ()-> Unit) {
    val configuration = LocalConfiguration.current // probably not the best way to get the size
    val width = configuration.screenWidthDp.dp
    val sizePx = with(LocalDensity.current) { width.toPx() }
    val swipeState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, sizePx to 1)

    if (swipeState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeState.currentValue) {
                    1 -> {
                        navController.popBackStack()
                    }
                    else -> return@onDispose
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.7f) },
                orientation = Orientation.Horizontal,
            )
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
                .fillMaxSize()
        ) {
            content()
        }
    }

}

fun NavGraphBuilder.birdNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    navigation( startDestination = SubScreen.BirdList.route, route = Screen.Birds.route ) {

        composable(route = SubScreen.BirdList.route) {
            Box(Modifier.padding(innerPadding)) {
                BirdListScreen(navController = navController)
            }
        }

        composable(SubScreen.BirdDetails.route, arguments = listOf(
                navArgument(SubScreen.BirdDetails.birdId) { type = NavType.StringType },
                navArgument(SubScreen.BirdDetails.birdName) { type = NavType.StringType}
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

@Composable
fun NavigationBar(navController: NavHostController, items: List<Screen>, showNavigationBar: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = showNavigationBar.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.surface
        ) {
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
                        if (currentDestination?.hierarchy?.any {it.route == screen.route} == false) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    })
            }
        }
    }
}
