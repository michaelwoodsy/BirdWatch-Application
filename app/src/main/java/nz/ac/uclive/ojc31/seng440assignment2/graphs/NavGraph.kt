package nz.ac.uclive.ojc31.seng440assignment2.graphs

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import nz.ac.uclive.ojc31.seng440assignment2.datastore.StoreOnboarding
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.screens.*
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.AddEntryScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdDetailsScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdListScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.CameraScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.SelectLocationScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.entry.ViewEntryScreen
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun NavGraph(
    navController: NavHostController,
    service: WeeklyNotificationService
) {
    val showNavigationBar = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val context = LocalContext.current
    val onboardingDataStore = StoreOnboarding(context)
    val onboardingState = onboardingDataStore.getOnboardingState.collectAsState(initial = true)

    val screens = listOf(
        Screen.Map,
        Screen.Home,
        Screen.Birds,
        Screen.History,
    )


    when (destination?.route) {
        Screen.Splash.route -> showNavigationBar.value = false
        Screen.Onboarding.route -> showNavigationBar.value = false
        SubScreen.BirdDetails.route -> showNavigationBar.value = false
        SubScreen.AddEntryDetails.route -> showNavigationBar.value = false
        SubScreen.SelectLocationScreen.route -> showNavigationBar.value = false
        SubScreen.CameraScreen.route -> showNavigationBar.value = false
        SubScreen.Settings.route -> showNavigationBar.value = false
        SubScreen.ViewEntryScreen.route -> showNavigationBar.value = false
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
        if (onboardingState.value!!) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
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
                composable(route = Screen.Home.route) {
                    Box(Modifier.padding(innerPadding)) {
                        HomeScreen(
                            navController,
                            permissions = listOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                ),
                            service = service
                        )
                    }
                }
                composable(route = Screen.Map.route) {
                    Box(Modifier.padding(innerPadding)) {
                        MapScreen(navController = navController)
                    }
                }
                composable(route = Screen.History.route) {
                    Box(Modifier.padding(innerPadding)) {
                        BirdHistoryScreen(navController = navController)
                    }
                }
                birdNavGraph(navController = navController, innerPadding = innerPadding)
                entryNavGraph(navController = navController, navBackStackEntry = navBackStackEntry)
                composable(route = SubScreen.Settings.route) {
                    Box(Modifier.padding(innerPadding)) {
                        SettingsScreen(navController = navController)
                    }
                }

                composable(route = SubScreen.ViewEntryScreen.route) {
                    val backStackEntry = remember(navBackStackEntry) {navController.getBackStackEntry(Screen.History.route)}
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
        } else {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
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
                composable(route = Screen.Home.route) {
                    Box(Modifier.padding(innerPadding)) {
                        HomeScreen(
                            navController = navController,
                            permissions = listOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                ),
                            service = service
                        )
                    }
                }
                composable(route = Screen.Map.route) {
                    Box(Modifier.padding(innerPadding)) {
                        MapScreen(navController = navController)
                    }
                }
                composable(route = Screen.History.route) {
                    Box(Modifier.padding(innerPadding)) {
                        BirdHistoryScreen(navController = navController)
                    }
                }
                birdNavGraph(navController = navController, innerPadding = innerPadding)
                entryNavGraph(navController = navController, navBackStackEntry = navBackStackEntry)

                composable(route = SubScreen.Settings.route) {
                    Box(Modifier.padding(innerPadding)) {
                        SettingsScreen(navController = navController)
                    }
                }
                composable(route = SubScreen.ViewEntryScreen.route) {
                    val backStackEntry = remember(navBackStackEntry) {navController.getBackStackEntry(Screen.History.route)}
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
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToReturn(navController: NavHostController, content: @Composable () -> Unit) {
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
    navigation(startDestination = SubScreen.BirdList.route, route = Screen.Birds.route) {

        composable(route = SubScreen.BirdList.route) {
            Box(Modifier.padding(innerPadding)) {
                BirdListScreen(navController = navController)
            }
        }

        composable(SubScreen.BirdDetails.route, arguments = listOf(
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


fun NavGraphBuilder.entryNavGraph(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
) {

    composable(SubScreen.AddEntryDetails.route, arguments = listOf(
        navArgument(SubScreen.AddEntryDetails.birdId) { type = NavType.StringType },
        navArgument(SubScreen.AddEntryDetails.birdName) { type = NavType.StringType },
        navArgument(SubScreen.AddEntryDetails.lat) { type = NavType.StringType },
        navArgument(SubScreen.AddEntryDetails.long) { type = NavType.StringType }
    )
    ) {
        var birdId = remember { it.arguments?.getString(SubScreen.AddEntryDetails.birdId) }
        var birdName = remember { it.arguments?.getString(SubScreen.AddEntryDetails.birdName) }
        var lat = remember { it.arguments?.getString(SubScreen.AddEntryDetails.lat) }
        var long = remember { it.arguments?.getString(SubScreen.AddEntryDetails.long) }

        if (birdId == "default") {
            birdId = ""
        }
        if (birdName == "default") {
            birdName = ""
        }
        if (lat == "default") {
            lat = "200"
        }
        if (long == "default") {
            long = "200"
        }

        SwipeToReturn(navController = navController) {
            AddEntryScreen(
                navController = navController,
            )
        }
    }


    composable(route = SubScreen.SelectLocationScreen.route) {
        val backStackEntry = remember(navBackStackEntry) {navController.getBackStackEntry(SubScreen.AddEntryDetails.route)}
        SelectLocationScreen(navController = navController, viewModel = hiltViewModel(backStackEntry))
    }

    composable(route = SubScreen.CameraScreen.route) {
        val backStackEntry = remember(navBackStackEntry) {navController.getBackStackEntry(SubScreen.AddEntryDetails.route)}

        SwipeToReturn(navController = navController) {
            CameraScreen(navController = navController, viewModel = hiltViewModel(backStackEntry))
        }
    }

}



@Composable
fun NavigationBar(
    navController: NavHostController,
    items: List<Screen>,
    showNavigationBar: MutableState<Boolean>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    AnimatedVisibility(
        visible = showNavigationBar.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.surface
        ) {


            items.forEach { screen ->
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    label = { Text(stringResource(screen.description)) },
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = "Navigation Item"
                        )
                    },
                    onClick = {
                        if (currentDestination?.hierarchy?.any { it.route == screen.route } == false) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
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

