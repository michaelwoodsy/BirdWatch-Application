package nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
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
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdDetailViewModel

@Composable
fun BirdDetailsScreen(
//    dominantColor: Color,
    birdId: String,
    navController: NavHostController,
    topPadding: Dp = 20.dp,
    birdImageSize: Dp = 200.dp,
    viewModel: BirdDetailViewModel = hiltViewModel()
) {
    val birdInfo = produceState<Resource<Birds>>(initialValue = Resource.Loading()) {
        value = viewModel.getBirdInfo(birdId)
    }.value
    val birdImageUrl by remember { viewModel.birdImageUrl }

    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Box {
                BirdDetailsTopSection(
//                    dominantColor = dominantColor,
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
//                    .background(dominantColor)
                        .align(Alignment.BottomCenter)
                )
                BirdDetailsStateWrapper(
                    birdInfo = birdInfo,
                    birdImageUrl = birdImageUrl,
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
                    ) {
                        if (birdInfo is Resource.Success) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(birdImageUrl)
                                    .build(),
                                contentDescription = birdInfo.data!![0].comName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(birdImageSize)
                                    .offset(y = topPadding),
                                loading = {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colors.primary,
                                        modifier = Modifier.scale(0.5f)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        else -> {
            Box {
                BirdDetailsLeftSection(
//                    dominantColor = dominantColor,
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
//                    .background(dominantColor)
                        .align(Alignment.CenterEnd)
                )
                BirdDetailsStateWrapper(
                    birdInfo = birdInfo,
                    birdImageUrl = birdImageUrl,
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
fun BirdDetailsLeftSection(
//    dominantColor: Color,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.surface,
//                        dominantColor
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
fun BirdDetailsTopSection(
//    dominantColor: Color,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.surface,
//                        dominantColor
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
fun BirdDetailsStateWrapper(
    birdInfo: Resource<Birds>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    birdImageUrl: String,
) {
    when (birdInfo) {
        is Resource.Success -> {
            val configuration = LocalConfiguration.current
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    BirdDetailsSection(
                        birdInfo = birdInfo.data!!,
                        modifier = modifier
                            .offset(y = (-10).dp),
                        birdImageUrl = birdImageUrl
                    )
                }
                else -> {
                    BirdDetailsSection(
                        birdInfo = birdInfo.data!!,
                        modifier = modifier,
                        birdImageUrl = birdImageUrl
                    )
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
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun BirdDetailsSection(
    birdInfo: Birds,
    birdImageUrl: String,
    modifier: Modifier,
    birdImageSize: Dp = 200.dp,
    topPadding: Dp = 20.dp,
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
//                PokemonTypeSection(types = pokemonInfo.types)
//                PokemonDetailDataSection(
//                    pokemonWeight = pokemonInfo.weight,
//                    pokemonHeight = pokemonInfo.height
//                )
//                PokemonBaseStats(pokemonInfo = pokemonInfo)
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
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(birdImageUrl)
                                .build(),
                            contentDescription = birdInfo[0].comName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(birdImageSize)
                                .offset(y = topPadding),
                            loading = {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.scale(0.5f)
                                )
                            }
                        )
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
//                PokemonTypeSection(types = pokemonInfo.types)
//                PokemonDetailDataSection(
//                    pokemonWeight = pokemonInfo.weight,
//                    pokemonHeight = pokemonInfo.height
//                )
//                PokemonBaseStats(pokemonInfo = pokemonInfo)
            }
        }
    }
}

//@Composable
//fun PokemonTypeSection(types: List<Type>) {
//    Row (
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .padding(16.dp)
//    ) {
//        for (type in types) {
//            Box (
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 8.dp)
//                    .clip(CircleShape)
//                    .background(parseTypeToColor(type))
//                    .height(35.dp)
//            ) {
//                Text(
//                    text = stringResource(id = (parseTypeToName(type))),
//                    color = Color.White,
//                    fontSize = 18.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun PokemonDetailDataSection(
//    pokemonWeight: Int,
//    pokemonHeight: Int,
//    sectionHeight: Dp = 80.dp
//) {
//    val pokemonWeightInKg = remember {
//        round(pokemonWeight * 100f) / 1000f
//    }
//    val pokemonHeightInMeters = remember {
//        round(pokemonHeight * 100f) / 1000f
//    }
//    Row (
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        PokemonDetailDataItem(
//            dataValue = pokemonWeightInKg,
//            dataUnit = "kg",
//            dataIcon = painterResource(id = R.drawable.ic_weight),
//            modifier = Modifier.weight(1f)
//        )
//        Spacer(modifier = Modifier
//            .size(1.dp, sectionHeight)
//            .background(Color.LightGray)
//        )
//        PokemonDetailDataItem(
//            dataValue = pokemonHeightInMeters,
//            dataUnit = "m",
//            dataIcon = painterResource(id = R.drawable.ic_height),
//            modifier = Modifier.weight(1f)
//        )
//    }
//}
//
//@Composable
//fun PokemonDetailDataItem(
//    dataValue: Float,
//    dataUnit: String,
//    dataIcon: Painter,
//    modifier: Modifier = Modifier
//) {
//    Column (
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier =  modifier
//    ) {
//        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colors.onSurface)
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = "$dataValue$dataUnit",
//            color = MaterialTheme.colors.onSurface
//        )
//    }
//}
//
//@Composable
//fun PokemonStat(
//    statName: String,
//    statValue: Int,
//    statMaxValue: Int,
//    statColor: Color,
//    height: Dp = 28.dp,
//    animDuration: Int = 1000,
//    animDelay: Int = 0
//) {
//    var animationPlayed by remember {
//        mutableStateOf(false)
//    }
//    var statRatio = statValue / statMaxValue.toFloat()
//    if (statRatio < 0.25f) {
//        statRatio = 0.25f
//    }
//    val curPercent = animateFloatAsState(
//        targetValue = if(animationPlayed) {
//            statRatio
//        } else 0f,
//        animationSpec = tween(
//            animDuration,
//            animDelay
//        )
//    )
//    val curText = animateFloatAsState(
//        targetValue = if(animationPlayed) {
//            statValue / statMaxValue.toFloat()
//        } else 0f,
//        animationSpec = tween(
//            animDuration,
//            animDelay
//        )
//    )
//    LaunchedEffect(key1 = true) {
//        animationPlayed = true
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(height)
//            .clip(CircleShape)
//            .background(
//                if (isSystemInDarkTheme()) {
//                    Color(0xFF505050)
//                } else {
//                    Color.LightGray
//                }
//            )
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth(curPercent.value)
//                .clip(CircleShape)
//                .background(statColor)
//                .padding(horizontal = 8.dp)
//        ) {
//            Text(
//                text = statName,
//                fontWeight = FontWeight.Bold,
//            )
//            Text(
//                text = (curText.value * statMaxValue).toInt().toString(),
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
//
//@Composable
//fun PokemonBaseStats(
//    pokemonInfo: Pokemon,
//    animDelayPerItem: Int = 100
//) {
//    val maxBaseStat = remember {
//        pokemonInfo.stats.maxOf { it.base_stat }
//    }
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = stringResource(id = R.string.base_stats),
//            fontSize = 20.sp,
//            color = MaterialTheme.colors.onSurface
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//
//        for(i in pokemonInfo.stats.indices) {
//            val stat = pokemonInfo.stats[i]
//            PokemonStat(
//                statName = stringResource(id = parseStatToAbbr(stat)),
//                statValue = stat.base_stat,
//                statMaxValue = maxBaseStat,
//                statColor = parseStatToColor(stat),
//                animDelay = i * animDelayPerItem
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//    }
//}