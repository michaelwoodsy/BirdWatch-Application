package nz.ac.uclive.ojc31.seng440assignment2.screens.entry

import android.content.ContentUris
import android.content.res.Configuration
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.delay
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
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box {
                BirdDetailTopSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .align(Alignment.TopCenter),
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
                )
                Box(modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
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

@Composable
fun BirdDetailLeftSection(
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
fun BirdDetailTopSection(
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
fun BirdDetailStateWrapper(
    entry: EntryDTO,
    modifier: Modifier = Modifier,
) {
    var showBirdInfo = remember{ mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        delay(0.5.seconds)
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

@Composable
fun BirdDetailSection(
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
                Row(Modifier.weight(1.2f)) {
                    Text(
                        text = entry.bird.comName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Row(Modifier.weight(1.2f)) {
                    Text(
                        text = entry.observedLocation,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Row(Modifier.weight(1.2f)) {
                    Text(
                        text = entry.observedDate.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
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
                Row(Modifier.weight(1.2f)) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.TopCenter) {
                        BirdImage(imageId = entry.imageId)
                    }
                    Text(
                        text = entry.bird.comName,
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
                Row(Modifier.weight(1.2f)) {
                    Text(
                        text = entry.observedLocation,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Row(Modifier.weight(1.2f)) {
                    Text(
                        text = entry.observedDate.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}