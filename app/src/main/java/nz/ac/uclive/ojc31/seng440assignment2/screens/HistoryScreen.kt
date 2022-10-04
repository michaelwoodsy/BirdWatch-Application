package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.data.EntryDTO
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.RobotoCondensed
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdHistoryViewModel
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.BirdListViewModel


@Composable
fun BirdHistoryScreen(navController: NavHostController, viewModel: BirdHistoryViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Column {
                    HistoryList(navController = navController)
                }
            }
            else -> {
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
    val hasLoaded by remember { viewModel.hasLoaded }

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
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            val itemCount = historyList.size
            items(itemCount) {
                if (!hasLoaded && !isLoading) {
                    LaunchedEffect(key1 = true) {
                        viewModel.loadEntries()
                    }
                }
                HistoryRow(rowIndex = it, entries = historyList, navController = navController)
            }
        }
    }


}

@Composable
fun HistoryEntry(
    entry: EntryDTO,
    navController: NavHostController,
    viewModel: BirdListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember {
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
                Text(
                    text = entry.bird.comName,
                    fontFamily = RobotoCondensed,
                    fontSize = 36.sp,
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
            ) {

                Text(
                    text = entry.bird.comName,
                    fontFamily = RobotoCondensed,
                    fontSize = 50.sp,
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
fun HistoryRow(
    rowIndex: Int,
    entries: List<EntryDTO>,
    navController: NavHostController
) {
    Row {
        HistoryEntry(
            entry = entries[rowIndex],
            navController = navController,
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}