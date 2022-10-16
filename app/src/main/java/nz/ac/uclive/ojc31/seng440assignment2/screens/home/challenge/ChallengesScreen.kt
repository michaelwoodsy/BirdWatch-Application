package nz.ac.uclive.ojc31.seng440assignment2.screens.home.challenge

import android.content.Context
import android.content.res.Configuration
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.data.challenges.ChallengeDTO
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.RobotoCondensed
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.ChallengeListViewModel

@Composable
fun ChallengesScreen(
    navController: NavHostController,
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {topSection(navController = navController)}
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            ChallengeListStateWrapper(navController)
        }
    }

    LaunchedEffect(Unit) {
        Toast.makeText(context, "Select a challenge to try complete it!", Toast.LENGTH_SHORT ).show()
    }

}


@Composable
private fun ChallengeListStateWrapper(
    navController: NavHostController,
    challengeListViewModel: ChallengeListViewModel = hiltViewModel()
) {
    val challenges = remember{challengeListViewModel.challenges}
    when (challengeListViewModel.isLoading.value) {
        true -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .align(Alignment.Center)
                )
            }
        }
        else -> {
            if (challengeListViewModel.loadError.value.isEmpty()){
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    val itemCount = challenges.size
                    items(itemCount) {
                        ChallengeRow(rowIndex = it, entries = challenges, navController = navController)
                    }
                }
            } else {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(challengeListViewModel.loadError.value)
                }
            }

        }
    }
}

@Composable
private fun ChallengeRow(rowIndex: Int, entries: List<ChallengeDTO>, navController: NavHostController) {
    Row(
        Modifier.clickable {  }
    ) {
        ChallengeEntry(challenge = entries[rowIndex])
    }
    Spacer(modifier = Modifier.height(16.dp))
}


private fun getAddress(context: Context, lat: Double, long: Double) : String {
    val geoCoder = Geocoder(context)
    val addressList = geoCoder.getFromLocation(lat, long, 1)
    return if (addressList.isNotEmpty()) {
        val address = addressList[0]
        address.locality ?: address.subAdminArea ?: address.adminArea ?: "Latitude: $lat, Longitude: $long"
    } else {
        "Latitude: $lat, Longitude: $long"
    }
}

@Composable
private fun ChallengeEntry(challenge: ChallengeDTO) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    var visibleAddress by remember {
        mutableStateOf("")
    }

    visibleAddress = getAddress(context, challenge.challenge.lastSeenLat, challenge.challenge.lastSeenLong)

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Row(
                Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colors.surface,
                                MaterialTheme.colors.surface
                            )
                        )
                    ),
                horizontalArrangement = Arrangement.Center

            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = challenge.bird.comName,
                        fontFamily = RobotoCondensed,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(textAlign = TextAlign.Center, text = buildAnnotatedString {
                        append("Spotted near ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(visibleAddress)
                        }
                    })
                }
            }
        }
        else -> {
            Row(
                Modifier
                    .padding(horizontal = 40.dp)
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colors.surface,
                                MaterialTheme.colors.surface
                            )
                        )
                    ),
                horizontalArrangement = Arrangement.Center

            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = challenge.bird.comName,
                        fontFamily = RobotoCondensed,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(textAlign = TextAlign.Center, text = buildAnnotatedString {
                        append("Spotted near ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(visibleAddress)
                        }
                    })
                }
            }
        }
    }
}

@Composable
private fun topSection(navController: NavHostController) {
    Box(
        Modifier.padding(bottom = 30.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}