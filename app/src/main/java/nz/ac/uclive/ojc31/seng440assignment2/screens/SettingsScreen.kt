package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavHostController
import nz.ac.uclive.ojc31.seng440assignment2.databinding.PreferencesContainerViewBinding


@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = { SettingsTopBar(navController = navController)},
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            AndroidViewBinding(PreferencesContainerViewBinding::inflate)
        }
    }
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