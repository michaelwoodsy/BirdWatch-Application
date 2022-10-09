package nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.DatePicker
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AddEntryViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdListViewModel

@Composable
fun AddEntryScreen(
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
) {

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
                        OpenCameraButton(navController = navController)
                    }
                }
            }
        }
        else -> {
            Box {
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
                AddEntryForm(
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
    birdListViewModel: BirdListViewModel = hiltViewModel(),
) {
    val configuration = LocalConfiguration.current
    if (viewModel.currentBirdName.value == "") {
        viewModel.currentBirdName.value = stringResource(R.string.no_bird_selected_label)
    }

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
                    Row(Modifier.weight(1f)) {
                        Text(
                            text = viewModel.currentBirdName.value,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    Row(Modifier.weight(1f)) {
                        SearchBar(
                            hint = stringResource(R.string.bird_entry_hint),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                        ) {
                            birdListViewModel.searchBirdList(it)
                        }
                    }
                    Row(
                        Modifier
                            .weight(2f)
                            .padding(5.dp)
                    ) {
                        BirdList(navController = navController, fromEntry = true)
                    }
                    Row(Modifier.weight(2f)) {
                        SelectLocationButton(navController = navController)
                    }
                    Row(Modifier.weight(1.2f)) {
                        DatePickButton()
                    }
                    Row(Modifier.weight(1f)) {
                        CancelButton(navController)
                        SaveButton(navController)
                    }
                    Row(Modifier.height(100.dp)) {
                    }

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

@Composable
fun SaveButton(
    navController: NavHostController,
    viewModel: AddEntryViewModel = hiltViewModel(),
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
                navController.popBackStack()
            }
        },
        enabled = (viewModel.canSave())

    ) {
        Text(text = stringResource(R.string.save_label))
    }
}

@Composable
fun CancelButton(
    navController: NavHostController,
) {
    Button(
        modifier = Modifier
            .width(100.dp)
            .padding(5.dp),
        onClick = {
            navController.popBackStack()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 128, 128))
    ) {
        Text(text = stringResource(R.string.cancel_button))
    }
}


@Composable
fun DatePickButton(
    viewModel: AddEntryViewModel = hiltViewModel(),
) {


    val datePicker = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            viewModel.datePicked.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, viewModel.year, viewModel.month, viewModel.day
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(5.dp)
                .width(130.dp),
            text = stringResource(R.string.date_label),
            fontSize = 20.sp,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(5.dp)
                .offset(y = (-7).dp)
                .width(130.dp),
            onClick = { datePicker.show() },
        ) {
            Text(text = viewModel.datePicked.value, fontSize = 15.sp, textAlign = TextAlign.Center)
        }
    }

}

@Composable
fun SelectLocationButton(
    navController: NavHostController,
    viewModel: AddEntryViewModel = hiltViewModel(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(5.dp)
                .width(130.dp),
            text = stringResource(R.string.location_label),
            fontSize = 20.sp,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
        if (viewModel.currentRegion.value == "") {
            Text(
                modifier = Modifier
                    .width(130.dp),
                text = stringResource(R.string.no_location_label),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.onSurface
            )
        } else {
            Text(
                modifier = Modifier
                    .width(130.dp),

                text = viewModel.currentRegion.value,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colors.onSurface
            )
        }

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(5.dp)
                .offset(y = (-7).dp)
                .width(130.dp),
            onClick = { navController.navigate(SubScreen.SelectLocationScreen.route) },
        ) {
            Text(
                text = stringResource(R.string.open_map_button),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OpenCameraButton(
    navController: NavHostController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(5.dp)
                .offset(y = (-7).dp)
                .width(130.dp),
            onClick = { navController.navigate(SubScreen.CameraScreen.route) },
        ) {
            Text(
                text = "Open Camera",
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
