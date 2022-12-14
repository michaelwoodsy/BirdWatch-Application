package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.data.entries.EntryDTO
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.screens.home.ExtendedAddEntryButton
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.RobotoCondensed
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.TitleFont
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdHistoryViewModel
import java.time.format.DateTimeFormatter


@Composable
fun BirdHistoryScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            ExtendedAddEntryButton(navController)
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.bird_history_title),
                    fontFamily = TitleFont,
                    fontSize = 64.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                HistoryList(navController = navController)
            }
        }
    }
}

@Composable
fun HistoryList(
    navController: NavHostController,
    viewModel: BirdHistoryViewModel = hiltViewModel()
) {
    val historyList by remember { viewModel.historyList }
    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center)
            )
        }
    } else {
        if (historyList.isEmpty()) {
            Column(Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Outlined.Info, null, Modifier.size(80.dp))
                Text(stringResource(R.string.no_entries))
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                val itemCount = historyList.size
                items(itemCount) {
                    HistoryRow(rowIndex = it, entries = historyList, navController = navController)
                }
            }
        }

    }
}

@Composable
fun HistoryEntry(
    entry: EntryDTO,
) {
    val defaultDominantColor = MaterialTheme.colors.surface

    val dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    val configuration = LocalConfiguration.current

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

            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = entry.bird.comName,
                        fontFamily = RobotoCondensed,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            append(stringResource(R.string.found_on))
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                append(entry.observedDate.format(formatter))
                            }
                            append(stringResource(R.string.at))
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(entry.observedLocation)
                            }
                        }
                    )
                }

            }
        }
        else -> {
            Row (
                modifier = Modifier
                    .padding(horizontal = 40.dp)
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
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = entry.bird.comName,
                        fontFamily = RobotoCondensed,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            append(stringResource(R.string.found_on))
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                append(entry.observedDate.format(formatter))
                            }
                            append(stringResource(R.string.at))
                            withStyle(style= SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(entry.observedLocation)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryRow(
    rowIndex: Int,
    entries: List<EntryDTO>,
    navController: NavHostController,
    viewModel: BirdHistoryViewModel = hiltViewModel()
) {
    Row ( modifier = Modifier
        .clickable {
            viewModel.currentIndex.value = rowIndex
            navController.navigate(
                SubScreen.ViewEntryScreen.route,
            )

        },
            ) {
        HistoryEntry(
            entry = entries[rowIndex]
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}