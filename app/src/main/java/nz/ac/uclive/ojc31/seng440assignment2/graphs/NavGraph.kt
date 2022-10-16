package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.screens.*
import kotlin.math.roundToInt

@Composable
fun NavGraph(
    navController: NavHostController,
    service: WeeklyNotificationService
) {
    val showNavigationBar = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

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
        SubScreen.LoadEntryFromLink.route -> showNavigationBar.value = false
        SubScreen.Challenges.route -> showNavigationBar.value = false
        SubScreen.Achievements.route -> showNavigationBar.value = false
        SubScreen.Statistics.route -> showNavigationBar.value = false
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
            launchGraph(navController = navController)

            composable(route = Screen.Map.route) {
                Box(Modifier.padding(innerPadding)) {
                    MapScreen(navController = navController)
                }
            }

            homeGraph(navController = navController, innerPadding = innerPadding)
            historyGraph(navController = navController, innerPadding = innerPadding)
            birdNavGraph(navController = navController, innerPadding = innerPadding)
            entryNavGraph(navController = navController, navBackStackEntry = navBackStackEntry, service = service)
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

