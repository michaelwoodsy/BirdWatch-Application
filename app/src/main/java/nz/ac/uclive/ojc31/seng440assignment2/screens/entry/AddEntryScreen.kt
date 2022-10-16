package nz.ac.uclive.ojc31.seng440assignment2.screens.entry

import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.res.Configuration
import android.provider.MediaStore
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.BirdList
import nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist.SearchBar
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AddEntryViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdListViewModel

@Composable
fun AddEntryScreen(
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    service: WeeklyNotificationService,
    birdId: String,
    birdName: String,
    lat: String,
    long: String,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initFromArguments(context, birdId, birdName, lat, long)
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
                    service = service
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.TopCenter)
                        {
                            ChosenImage()
                        }
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
                    service = service
                )
            }
        }
    }

}


@Composable
fun AddEntryFormStateWrapper(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    service: WeeklyNotificationService
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
                    service = service
                )
            }
            else -> {
                AddEntryForm(
                    navController = navController,
                    modifier = modifier,
                    service = service
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
    service: WeeklyNotificationService
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
                    Row(Modifier.weight(1.3f)) {
                        OpenCameraButton(navController = navController, modifier = Modifier
                            .padding(5.dp)
                            .offset(y = 5.dp)
                            .width(150.dp))
                    }
                    Row(Modifier.weight(1f)) {
                        Text(
                            text = viewModel.currentBirdName.value,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
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
                    Row(Modifier.weight(1.2f)) {
                        CancelButton(navController)
                        SaveButton(navController, service = service)
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
                        Column {
                            Box(
                                modifier = Modifier,
                                contentAlignment = Alignment.TopCenter
                            ) {
                                ChosenImage()
                                OpenCameraButton(navController = navController, modifier = Modifier
                                    .padding(5.dp)
                                    .offset(y = 220.dp)
                                    .width(150.dp))
                            }
                        }
                        Column {
                            Row(Modifier.weight(1f)) {
                                Text(
                                    text = viewModel.currentBirdName.value,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically),
                                    overflow = TextOverflow.Ellipsis,
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
                            Row(Modifier.weight(1.2f)) {
                                CancelButton(navController)
                                SaveButton(navController, service = service)
                            }
                        }
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
    service: WeeklyNotificationService
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
                service.showNotification()
            }
        },
        enabled = (viewModel.canSave() && !viewModel.saving.value)

    ) {
        if (!viewModel.saving.value) {
            Text(text = stringResource(R.string.save_label))
        } else {
            Text(text = stringResource(R.string.saving_label))
        }
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
                fontSize = 12.sp,
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
fun ChosenImage(
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    viewModel: AddEntryViewModel = hiltViewModel(),
) {
    if (viewModel.imageId.value != "") {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, viewModel.imageId.value.toLong())
                    )
                .build(),
            contentDescription = "Bird Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(birdImageSize)
                .offset(y = topPadding),
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            },
        )
    } else {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    "https://www.justcolor.net/kids/wp-content/uploads/sites/12/nggallery/birds/coloring-pages-for-children-birds-82448.jpg"
                )
                .build(),
            contentDescription = "Bird Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(birdImageSize)
                .offset(y = topPadding),
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            },
        )
    }
}


@Composable
fun OpenCameraButton(
    navController: NavHostController,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier,
            onClick = { navController.navigate(SubScreen.CameraScreen.route) },
        ) {
            Text(
                text = stringResource(R.string.change_image_button),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
