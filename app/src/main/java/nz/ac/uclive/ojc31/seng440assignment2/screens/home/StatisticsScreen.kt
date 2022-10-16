package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsViewModel = hiltViewModel(),
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

    if (viewModel.isLoading()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .offset(y = (-20).dp)) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.Center)
            )
        }
    } else {
        Column {
            Row {
                Text(viewModel.entryCount.value)
            }
            Row {
                Text(viewModel.achievementsCount.value)
            }
            Row {
                Text(viewModel.mostRecentEntry.value)
            }
            Row {
                Text(viewModel.oldestEntry.value)
            }
            Row {
                Text(viewModel.mostCommonBird.value)
            }

        }

    }

}