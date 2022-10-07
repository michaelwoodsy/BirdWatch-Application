package nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.data.images.Images
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen
import nz.ac.uclive.ojc31.seng440assignment2.screens.ExtendedAddEntryButton
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AddEntryViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdDetailViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdListViewModel

@Composable
fun AddEntryScreen(
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    viewModel: AddEntryViewModel = hiltViewModel(),
    birdName: String,
    birdId: String,
    lat: String,
    long: String,

    ) {

    viewModel.currentBirdName.value = birdName
    viewModel.currentBirdId.value = birdId
    viewModel.currentLat.value = lat
    viewModel.currentLong.value = long


    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box {
                AddEntryTopSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .align(Alignment.TopCenter),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(MaterialTheme.colors.primary)
                        .align(Alignment.BottomCenter)
                )
                AddEntryFormStateWrapper(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = topPadding + birdImageSize / 2f,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    )
                    {
                        // Image
                    }
                }
            }
        }
        else -> {
            Box {
                AddEntryLeftSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colors.primary)
                        .align(Alignment.CenterEnd)
                )
                AddEntryFormStateWrapper(
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
                        .align(Alignment.BottomCenter),
                )
            }
        }
    }

}

@Composable
fun AddEntryLeftSection(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.primary
                    )
                )
            ),
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
                .align(Alignment.TopStart)
        )
    }
}

@Composable
fun AddEntryTopSection(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.primary
                    )
                )
            ),
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

@Composable
fun AddEntryFormStateWrapper(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val showForm = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showForm.value = true
    }
    AnimatedVisibility(
        visible = showForm.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = fadeOut(),


        ) {
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                AddEntryForm (
                    navController = navController,
                    modifier = modifier
                        .offset(y = (-10).dp),
                )
            }
            else -> {
                AddEntryForm(
                    navController = navController,
                    modifier = modifier,
                )
            }
        }
    }

}

@Composable
fun AddEntryForm(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: AddEntryViewModel = hiltViewModel(),
    ) {
    val scrollState = rememberScrollState()
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
                    Text(
                        text = viewModel.currentBirdName.value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                    BirdList(navController = navController, fromEntry = true)
                }
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    Row {
                        Box(
                            modifier = Modifier,
                            contentAlignment = Alignment.TopCenter
                        ) {
                            // Image
                        }
                        Text(
                            text = viewModel.currentBirdName.value,
                            fontWeight = FontWeight.Bold,
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterVertically),
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}

