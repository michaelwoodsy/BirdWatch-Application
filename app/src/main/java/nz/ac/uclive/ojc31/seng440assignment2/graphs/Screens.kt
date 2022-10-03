package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import nz.ac.uclive.ojc31.seng440assignment2.R

sealed class Screen(val route: String, @StringRes val description : Int, val icon: ImageVector) {
    object Splash : Screen(
        "splash_screen",
        R.string.screen_label_splash,
        Icons.Default.Home)

    object Home : Screen(
        "home_screen",
        R.string.screen_label_home,
        Icons.Default.Home)
}
