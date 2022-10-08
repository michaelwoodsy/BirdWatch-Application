package nz.ac.uclive.ojc31.seng440assignment2.screens.birdlist

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.RobotoCondensed
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.AddEntryViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdListViewModel

@Composable
fun BirdListScreen(
    navController: NavHostController,
    viewModel: BirdListViewModel = hiltViewModel(),
) {
    val configuration = LocalConfiguration.current
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.bird),
                        contentDescription = "BirdWatch",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                    SearchBar(
                        hint = stringResource(id = R.string.bird_search_hint),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        viewModel.searchBirdList(it)
                    }
                    BirdList(navController = navController)
                }
            }
            else -> {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.bird),
                            contentDescription = "BirdWatch",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .align(Alignment.CenterStart)
                                .padding(16.dp)
                        )
                        SearchBar(
                            hint = stringResource(id = R.string.bird_search_hint),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .align(Alignment.CenterEnd)
                                .padding(16.dp),
                        ) {
                            viewModel.searchBirdList(it)
                        }
                    }
                    BirdList(navController = navController)
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    viewModel: BirdListViewModel = hiltViewModel(),
    onSearch: (String) -> Unit = {},
) {
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    var searchText by remember {
        viewModel.searchText
    }

    val focusManager = LocalFocusManager.current

    Box(modifier = modifier){
        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(searchText)
            },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                isHintDisplayed
            }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )
        if(isHintDisplayed && searchText == ""){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun BirdList(
    navController: NavHostController,
    viewModel: BirdListViewModel = hiltViewModel(),
    fromEntry: Boolean = false,
) {
    val birdList by remember { viewModel.birdList }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    var loadingSize = 0.5f
    var padding = 16.dp
    if (fromEntry) {
        loadingSize = 0.2f
        padding = 0.dp
    }

    if (isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .offset(y = (-20).dp)) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxSize(loadingSize)
                    .align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(padding)) {
            val itemCount = birdList.size
            items(itemCount) {
                if(it >= itemCount -1 && !endReached && !isLoading && !isSearching) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadBirds()
                    }
                }
                BirdRow(rowIndex = it, entries = birdList, navController = navController, fromEntry = fromEntry)
            }
        }
    }
}

@Composable
fun BirdEntry(
    entry: BirdsItem,
    navController: NavHostController,
    fromEntry: Boolean = false,
    entryViewModel: AddEntryViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    val birdName = entry.comName
    val birdId = entry.speciesCode
    val dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    val configuration = LocalConfiguration.current

    var fontSizePort = 36.sp
    var fontSizeHor = 50.sp
    if (fromEntry) {
        fontSizePort = 18.sp
        fontSizeHor = 25.sp
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Row (
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                dominantColor,
                                defaultDominantColor
                            )
                        )
                    )
                    .clickable {
                        if (fromEntry) {
                            entryViewModel.currentBirdId.value = birdId
                            entryViewModel.currentBirdName.value = birdName
                        } else {
                            navController.navigate(
                                "bird_details_screen/${birdId}/${birdName}",
                            )
                        }
                    },
            ) {
                Text(
                    text = birdName,
                    fontFamily = RobotoCondensed,
                    fontSize = fontSizePort,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        else -> {
            Row (
                modifier = Modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                dominantColor,
                                defaultDominantColor
                            )
                        )
                    )
                    .clickable {
                        if (fromEntry) {
                            entryViewModel.currentBirdId.value = birdId
                            entryViewModel.currentBirdName.value = birdName
                        } else {
                            navController.navigate(
                                "bird_details_screen/${birdId}/${birdName}",
                            )
                        }
                    },
            ) {
                Text(
                    text = birdName,
                    fontFamily = RobotoCondensed,
                    fontSize = fontSizeHor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
fun BirdRow(
    rowIndex: Int,
    entries: List<BirdsItem>,
    navController: NavHostController,
    fromEntry: Boolean = false,
) {
    Row {
        BirdEntry(
            entry = entries[rowIndex],
            navController = navController,
            fromEntry = fromEntry,
        )
    }
    if (fromEntry) {
        Spacer(modifier = Modifier.height(8.dp))
    } else {
        Spacer(modifier = Modifier.height(16.dp))
    }
}