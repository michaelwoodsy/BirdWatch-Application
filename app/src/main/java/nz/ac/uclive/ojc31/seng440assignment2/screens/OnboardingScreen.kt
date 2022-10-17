package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.datastore.StoreOnboarding
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    permissions: List<String>
) {
    val pagerState = rememberPagerState(0)

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val onboardingDataStore = StoreOnboarding(context)

    Column {
        Button(
            shape = RoundedCornerShape(20.dp),
            onClick = {
                scope.launch {
                    onboardingDataStore.setOnboardingState(true)
                }
                multiplePermissionsState.launchMultiplePermissionRequest()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) {
                        inclusive = true // prevent back button returning to splash screen
                    }
                }
            },
            modifier = Modifier.align(Alignment.Start).padding(4.dp)
        ) {
            Text(
                text = "Skip", modifier = Modifier
            )
        }


        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            count = 3
        ) { page ->

            PageUI(page = onboardPages[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = MaterialTheme.colors.primary
        )

        AnimatedVisibility(visible = pagerState.currentPage == 2) {
            Button(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), onClick = {
                    scope.launch {
                        onboardingDataStore.setOnboardingState(true)
                    }
                    multiplePermissionsState.launchMultiplePermissionRequest()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true // prevent back button returning to splash screen
                        }
                    }
                },
            ) {
                Text(text = stringResource(R.string.app_name))
            }
        }

    }
}

@Composable
fun PageUI(page: Page) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = page.title,
            fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        )
        Text(
            text = page.description,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
        )
    }
}

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val onboardPages = listOf(
    Page(
        "Welcome to BirdWatch!",
        "BirdWatch is the perfect application for BirdWatchers across the globe.",
        R.drawable.ic_baseline_accessibility_24
    ),
    Page(
        "Find as many birds as possible!",
        "Add bird entries to your history and challenge your friends to find the same " +
                "birds as you.",
        R.drawable.ic_baseline_camera_front_24
    ),
    Page(
        "Please ensure to enable your phone's camera and location",
        "This is so you can take full advantage of BirdWatch's countless features. " +
                "If you don't approve it after exiting this screen, you can enable them later " +
                "in your phone's settings.",
        R.drawable.ic_baseline_perm_device_information_24
    )
)