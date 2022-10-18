package nz.ac.uclive.ojc31.seng440assignment2.screens.entry

import android.content.ContentUris
import android.content.res.Configuration
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDTO
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdHistoryViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun ViewEntryScreen(
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    historyViewModel: BirdHistoryViewModel = hiltViewModel()
) {
    val entry = historyViewModel.historyList.value[historyViewModel.currentIndex.value]
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val toastText = stringResource(R.string.copied_text)
    val onShare: ()-> Unit = {
        val deepLinkText = "birdwatch://share/${entry.bird.speciesCode}/${entry.lat}/${entry.long}"
        clipboardManager.setText(AnnotatedString(deepLinkText))
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
    }
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box(Modifier.background(color = MaterialTheme.colors.primary)) {
                BirdDetailTopSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .align(Alignment.TopCenter),
                    onShare = onShare
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(MaterialTheme.colors.primary)
                    .align(Alignment.BottomCenter)
                )
                BirdDetailStateWrapper(
                    entry = entry,
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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter)
                    {
                        BirdImage(imageId = entry.imageId)
                    }
                }
            }
        }
        else -> {
            Box {
                BirdDetailLeftSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    onShare = onShare
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(MaterialTheme.colors.primary)
                    .align(Alignment.CenterEnd)
                )
                BirdDetailStateWrapper(
                    entry = entry,
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
fun BirdImage(
    imageId: Int?,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
) {
    var birdImageUrl = "https://www.justcolor.net/kids/wp-content/uploads/sites/12/nggallery/birds/coloring-pages-for-children-birds-82448.jpg"

    if (imageId != null) {
        birdImageUrl = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toLong()).toString()
    }
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(birdImageUrl)
            .build(),
        contentDescription = stringResource(R.string.bird_image),
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

@Composable
private fun BirdDetailLeftSection(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShare: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary
                    )
                )
            ),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
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
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .size(36.dp)
                    .offset(16.dp, -(16.dp))
                    .clickable {
                        onShare()
                    }
            )
        }
    }
}

@Composable
private fun BirdDetailTopSection(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onShare: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primary
                    )
                )
            ),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .size(36.dp)
                    .offset(-(16.dp), 16.dp)
                    .clickable {
                        onShare()
                    }
            )
        }
    }
}

@Composable
private fun BirdDetailStateWrapper(
    entry: EntryDTO,
    modifier: Modifier = Modifier,
) {
    val showBirdInfo = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true)}

    LaunchedEffect(Unit) {
        delay(0.4.seconds)
        isLoading = false
    }

    when(isLoading) {
        false -> {
            LaunchedEffect(Unit) {
                showBirdInfo.value = true
            }
            AnimatedVisibility(
                visible = showBirdInfo.value,
                enter = slideInVertically(initialOffsetY = {it}),
                exit = fadeOut(),
            ) {
                val configuration = LocalConfiguration.current
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        BirdDetailSection(
                            modifier = modifier
                                .offset(y = (-10).dp),
                            entry = entry
                        )
                    }
                    else -> {
                        BirdDetailSection(
                            modifier = modifier,
                            entry = entry
                        )
                    }
                }
            }
        }
        true -> {
            Column(Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(
                            top = 120.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )

                )
            }

        }
    }

}

@Composable
private fun BirdDetailSection(
    entry: EntryDTO,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .offset(y = 100.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = entry.bird.comName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                Text(
                    text = entry.observedLocation,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = entry.observedDate.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.scientific_name_label))
                        }
                        append(entry.bird.sciName)
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(16.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.family_name_label))
                        }
                        append(entry.bird.familyComName)
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(16.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.scientific_family_name_label))
                        }
                        append(entry.bird.familySciName)
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(16.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.order_label))
                        }
                        append(entry.bird.order)
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(16.dp)
                )
                if (entry.bird.extinct) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.extinct_label))
                            }
                            append(stringResource(R.string.yes))
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp)
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.extinct_year_label))
                            }
                            append("${entry.bird.extinctYear}")
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp)
                    )
                } else {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.extinct_label))
                            }
                            append(stringResource(R.string.no))
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp)
                    )
                }
            }
        }
        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Row {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.TopCenter) {
                        BirdImage(imageId = entry.imageId)
                    }
                    Column(Modifier.fillMaxSize()) {
                        Text(
                            text = entry.bird.comName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                        Text(
                            text = entry.observedLocation,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = entry.observedDate.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stringResource(R.string.scientific_name_label))
                                }
                                append(entry.bird.sciName)
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(16.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stringResource(R.string.family_name_label))
                                }
                                append(entry.bird.familyComName)
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(16.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stringResource(R.string.scientific_family_name_label))
                                }
                                append(entry.bird.familySciName)
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(16.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stringResource(R.string.order_label))
                                }
                                append(entry.bird.order)
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(16.dp)
                        )
                        if (entry.bird.extinct) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.extinct_label))
                                    }
                                    append(stringResource(R.string.yes))
                                },
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(16.dp)
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.extinct_year_label))
                                    }
                                    append("${entry.bird.extinctYear}")
                                },
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(16.dp)
                            )
                        } else {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.extinct_label))
                                    }
                                    append(stringResource(R.string.no))
                                },
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}