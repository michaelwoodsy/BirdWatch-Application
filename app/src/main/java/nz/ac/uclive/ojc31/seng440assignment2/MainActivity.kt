package nz.ac.uclive.ojc31.seng440assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import nz.ac.uclive.ojc31.seng440assignment2.graphs.NavGraph
import nz.ac.uclive.ojc31.seng440assignment2.ui.theme.SENG440Assignment2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SENG440Assignment2Theme {
                val systemUiController = rememberSystemUiController()
                val navBarColor = MaterialTheme.colors.primary
                SideEffect {
                    systemUiController.setSystemBarsColor(color = navBarColor)
                }
                NavGraph(navController = rememberNavController())
            }
        }
    }
}