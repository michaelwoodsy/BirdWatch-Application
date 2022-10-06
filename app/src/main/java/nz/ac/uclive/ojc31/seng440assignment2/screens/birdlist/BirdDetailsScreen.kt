package nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.data.images.Images
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdDetailViewModel

@Composable
fun BirdDetailsScreen(
    birdId: String,
    birdName: String,
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    viewModel: BirdDetailViewModel =  hiltViewModel()
) {
    val birdInfo = produceState<Resource<Birds>>(initialValue = Resource.Loading()) {
        value =  viewModel.getBirdInfo(birdId)
    }.value

    val birdImages = produceState<Resource<Images>>(initialValue = Resource.Loading()) {
        value =  viewModel.getBirdImage(birdName)
    }.value
    
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
                    birdInfo = birdInfo,
                    birdsImages = birdImages,
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
                    loadingModifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .padding(
                            top = topPadding + birdImageSize / 2f,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
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
                        BirdImage(birdsImages = birdImages)
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
                    birdInfo = birdInfo,
                    birdsImages = birdImages,
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
                    loadingModifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .padding(
                            top = 16.dp,
                            start = 64.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                )
            }
        }
    }
}

@Composable
fun BirdImage(
    birdsImages: Resource<Images>,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
) {
    var birdImageUrl = "https://www.justcolor.net/kids/wp-content/uploads/sites/12/nggallery/birds/coloring-pages-for-children-birds-82448.jpg"

    when (birdsImages) {
        is Resource.Success -> {
            val photos = birdsImages.data!!.photos
            if (photos.total != 0) {
                val bestPhoto = photos.photo[0]
                val photoId = bestPhoto.id
                val serverId = bestPhoto.server
                val secret = bestPhoto.secret
                birdImageUrl = "https://live.staticflickr.com/${serverId}/${photoId}_${secret}.jpg"
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
        is Resource.Error -> {
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
        else -> {}
    }

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
    birdInfo: Resource<Birds>,
    birdsImages: Resource<Images>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
) {
    val showBirdInfo = remember{ mutableStateOf(false)}
    when(birdInfo) {
        is Resource.Success -> {
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
                            birdInfo = birdInfo.data!!,
                            birdsImages = birdsImages,
                            modifier = modifier
                                .offset(y = (-10).dp)
                        )
                    }
                    else -> {
                        BirdDetailSection(
                            birdInfo = birdInfo.data!!,
                            birdsImages = birdsImages,
                            modifier = modifier
                        )
                    }
                }
            }

        }
        is Resource.Error -> {
            Text(
                text = birdInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onSurface,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun BirdDetailSection(
    birdInfo: Birds,
    birdsImages: Resource<Images>,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()
    val birdName = birdInfo[0].comName
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
                    text = birdName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface
                )
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
                        BirdImage(birdsImages = birdsImages)
                    }
                    Text(
                        text = birdName,
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