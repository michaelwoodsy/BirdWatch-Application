package nz.ac.uclive.ojc31.seng440assignment2.graphs

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
}