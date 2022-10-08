package nz.ac.uclive.ojc31.seng440assignment2.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen
import nz.ac.uclive.ojc31.seng440assignment2.databinding.PreferencesContainerViewBinding
import nz.ac.uclive.ojc31.seng440assignment2.fragments.SettingsFragment
import nz.ac.uclive.ojc31.seng440assignment2.graphs.Screen
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen


@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = { SettingsTopBar(navController = navController)},
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            AndroidViewBinding(PreferencesContainerViewBinding::inflate)
        }
    }



    // How to access preferences
    //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
}

@Composable
fun SettingsTopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Text("BirdWatch Settings")
        },
        navigationIcon = {
             IconButton(
                 onClick = { navController.popBackStack() }) {
                 Icon(Icons.Default.ArrowBack, null)
             }
        },

    )
}