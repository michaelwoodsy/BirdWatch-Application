package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import nz.ac.uclive.ojc31.seng440assignment2.R

sealed class Screen(val route: String, @StringRes val description : Int, val icon: ImageVector) {
    object Splash : Screen(
        "splash_screen",
        R.string.screen_label_splash,
        Icons.Default.Home)

    object Map : Screen(
        "map_screen",
        R.string.screen_label_map,
        Icons.Default.LocationOn
    )

    object Home : Screen(
        "home_screen",
        R.string.screen_label_home,
        Icons.Default.Home)

    object BirdList : Screen(
        "bird_list_screen",
        R.string.screen_label_bird_list,
        Icons.Default.List
    )

    object History : Screen(
        "saved_entries",
        R.string.screen_label_history,
        Icons.Default.Info
    )
}
