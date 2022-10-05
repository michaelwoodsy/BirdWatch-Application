package nz.ac.uclive.ojc31.seng440assignment2.graphs

import androidx.annotation.StringRes
import nz.ac.uclive.ojc31.seng440assignment2.R

sealed class Screen(val route: String, @StringRes val description : Int, val icon: Int) {
    object Splash : Screen(
        "splash_screen",
        R.string.screen_label_splash,
        R.drawable.home)

    object Map : Screen(
        "map_screen",
        R.string.screen_label_map,
        R.drawable.map
    )

    object Home : Screen(
        "home_screen",
        R.string.screen_label_home,
        R.drawable.home)

    object Birds : Screen(
        "birds",
        R.string.screen_label_bird_list,
        R.drawable.bird
    )

    object History : Screen(
        "saved_entries",
        R.string.screen_label_history,
        R.drawable.history
    )
}

sealed class SubScreen(val route: String) {
    object BirdList : SubScreen("bird_list")
    object BirdDetails : SubScreen("bird_details_screen/{birdId}") {
        val birdId : String = "birdId"
    }
}
