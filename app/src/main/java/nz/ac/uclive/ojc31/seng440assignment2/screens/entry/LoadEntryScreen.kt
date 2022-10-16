package nz.ac.uclive.ojc31.seng440assignment2.screens.entry

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.LoadEntryViewModel


@Composable
fun LoadEntryScreen(
    navController: NavHostController,
    birdId: String,
    lat: Double,
    long: Double,
    viewModel: LoadEntryViewModel = hiltViewModel(),
    topPadding: Dp = 20.dp
) {
    LaunchedEffect(Unit) {
        viewModel.loadEntry(birdId, lat, long)
    }
    
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(MaterialTheme.colors.primary)
                        .align(Alignment.BottomCenter)
                )
                LoadEntryStateWrapper(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = topPadding,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
        else -> {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(MaterialTheme.colors.primary)
                        .align(Alignment.BottomCenter)
                )
                LoadEntryStateWrapper(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            start = 64.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun LoadEntryStateWrapper(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoadEntryViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    when (viewModel.isLoading.value || viewModel.errorMessage.value.isNotEmpty()) {
        true -> {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            } else {
                Text(text = viewModel.errorMessage.value)
            }
        }
        else -> {
            AnimatedVisibility(
                visible = !viewModel.isLoading.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = fadeOut()
            ) {
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        LoadEntryForm(
                            navController = navController,
                            modifier = modifier.offset(y = (-10).dp)
                        )
                    }
                    else -> {
                        LoadEntryForm(
                            navController = navController,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LoadEntryForm(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: LoadEntryViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxSize()
                        .offset(y = 100.dp)
                ) {
                    Row(Modifier.weight(0.5f)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Someone spotted a")
                            Text(
                                text = viewModel.bird.value!!.comName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                    Row(Modifier.weight(2.0f)) {
                        Box(Modifier.padding(top=20.dp, bottom = 20.dp)){
                            LocationViewer()
                        }

                    }
                    Row(Modifier.weight(1.0f)) {
                        Column() {
                            Text("Do you accept this challenge?")
                            Row() {
                                CancelButton(navController = navController)
                                SaveButton(navController = navController)
                            }
                        }
                    }
                }
            }
            else -> {

            }
        }
    }

}


@Composable
private fun SaveButton(
    navController: NavHostController,
    viewModel: LoadEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    Button(
        modifier = Modifier
            .width(100.dp)
            .padding(5.dp),
        onClick = {
            coroutineScope.launch {
                viewModel.saveEntry(ctx)
                navController.navigate(SubScreen.Home.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        },
        enabled = !viewModel.isSaving.value
    ) {
        if (!viewModel.isSaving.value) {
            Text("Accept")
        } else {
            Text(text = stringResource(R.string.saving_label))
        }
        
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun LocationViewer(viewModel: LoadEntryViewModel = hiltViewModel()) {
    val uiSettings = remember {
        MapUiSettings()
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
            mapType = MapType.HYBRID,
            isMyLocationEnabled = true)
        )
    }
    val spottedLocation = LatLng(viewModel.lat.value, viewModel.long.value)
    val birdLocationState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(spottedLocation, 15f)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            Modifier.fillMaxSize(),
            cameraPositionState = birdLocationState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            Marker(
                state = MarkerState(position = spottedLocation),
                title = "${viewModel.bird.value?.comName}",
                draggable = false
            )
        }
    }
}















