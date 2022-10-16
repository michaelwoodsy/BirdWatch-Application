package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.R
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    Box {

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

        if (viewModel.isLoading()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-20).dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .align(Alignment.Center)
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                val modifier = Modifier.width(200.dp)
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.total_entries_label),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier,
                    )
                    Text(
                        text = viewModel.entryCount.value,
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        modifier = modifier,
                    )
                }
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.total_achievements_label),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier,
                    )
                    Text(
                        text = viewModel.achievementsCount.value,
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        modifier = modifier,
                    )
                }
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.most_recent_label),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier,
                    )
                    Text(
                        text = viewModel.mostRecentEntry.value,
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        modifier = modifier,
                    )
                }
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.first_entry_label),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier,
                    )
                    Text(
                        text = viewModel.oldestEntry.value,
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        modifier = modifier,
                    )
                }
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.most_common_label),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier,
                    )
                    Text(
                        text = viewModel.mostCommonBird.value,
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        modifier = modifier,
                    )
                }
            }

        }
    }

}