package nz.ac.uclive.ojc31.seng440assignment2.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AchievementsScreen(
    navController: NavHostController
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