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

    object Onboarding : Screen(
        "onboarding",
        R.string.screen_label_home,
        R.drawable.home
    )
}

sealed class SubScreen(val route: String) {
    object BirdList : SubScreen("bird_list")
    object Challenges : SubScreen("challenges")
    object Achievements : SubScreen("achievements")
    object Statistics : SubScreen("statistics")
    object BirdDetails : SubScreen("bird_details_screen/{birdId}/{birdName}") {
        const val birdId : String = "birdId"
        const val birdName : String = "birdName"
    }
    object Settings : SubScreen("settings")
    object AddEntryDetails : SubScreen("add_entry_screen?birdId={birdId}&birdName={birdName}&lat={lat}&long={long}&challengeId={challengeId}") {
        const val birdId : String = "birdId"
        const val birdName : String = "birdName"
        const val lat : String = "lat"
        const val long : String = "long"
        const val challengeId : String = "challengeId"
    }
    object SelectLocationScreen : SubScreen("select_location")
    object CameraScreen : SubScreen("camera_view")
    object ViewEntryScreen : SubScreen("view_entry")

    object LoadEntryFromLink : SubScreen("load_entry/{birdId}/{lat}/{long}") {
        const val deepLinkURL : String = "birdwatch://share/{birdId}/{lat}/{long}"
        const val birdId : String = "birdId"
        const val lat : String = "lat"
        const val long : String = "long"
    }
}

