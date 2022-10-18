package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.datastore.StoreOnboarding
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.TitleFont

@Composable
fun SplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    val context = LocalContext.current
    val onboardingDataStore = StoreOnboarding(context)
    val onboardingState = onboardingDataStore.getOnboardingState.collectAsState(initial = false)

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        if (onboardingState.value!!) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true // prevent back button returning to splash screen
                }
            }
        } else {
            navController.navigate(Screen.Onboarding.route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true // prevent back button returning to splash screen
                }
            }
        }
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.bird),
                contentDescription = stringResource(R.string.logo_icon),
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha = alpha)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = TitleFont,
                fontSize = 40.sp,
                color = Color.White,
                modifier = Modifier
                    .alpha(alpha)
                )
        }
    }
}
