package nz.ac.uclive.ojc31.seng440assignment2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import nz.ac.uclive.ojc31.seng440assignment2.fragments.SettingsViewModel
import nz.ac.uclive.ojc31.seng440assignment2.graphs.NavGraph
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.SENG440Assignment2Theme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            val appTheme by remember {settingsViewModel.appTheme}
            val useDarkTheme = when(appTheme) {
                "dark_theme" -> true
                "light_theme" -> false
                else -> isSystemInDarkTheme()
            }

            val service = WeeklyNotificationService(applicationContext)

            SENG440Assignment2Theme(darkTheme = useDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val navBarColor = MaterialTheme.colors.primary
                SideEffect {
                    systemUiController.setSystemBarsColor(color = navBarColor)
                }
                NavGraph(navController = rememberNavController(), service)
            }
        }
    }
}

