package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(0)

    Column() {

        Text(text = "Skip",modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) {
                        inclusive = true // prevent back button returning to splash screen
                    }
                }
            }
        )

        HorizontalPager(state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        count = 3) { page ->

            PageUI(page = onboardPages[page])
        }

        HorizontalPagerIndicator(pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = MaterialTheme.colors.primary
        )

        AnimatedVisibility(visible = pagerState.currentPage == 2 ) {
            OutlinedButton(shape = RoundedCornerShape(20.dp) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true // prevent back button returning to splash screen
                        }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White)) {
                Text(text = stringResource(R.string.app_name))
            }
        }

    }
}

@Composable
fun PageUI(page: Page) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = page.title,
            fontSize = 28.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = page.description,
            textAlign = TextAlign.Center,fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

    }
}

data class Page(val title: String,
                val description: String,
                @DrawableRes val image:Int)

val onboardPages = listOf(
    Page(
        "Welcome to BirdWatch",
        "This app is really really good",
        R.drawable.bird
    ),
    Page(
        "Trust me, this app is sick",
        "You're gonna have such ana awesome time on BirdWatch",
        R.drawable.bird
    ),
    Page(
        "Please enable your camera and location",
        "You can also do so in your phone's settings",
        R.drawable.bird
    )
)