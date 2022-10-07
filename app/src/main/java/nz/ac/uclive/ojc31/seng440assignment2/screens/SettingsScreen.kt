package nz.ac.uclive.ojc31.seng440assignment2.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.findFragment
import nz.ac.uclive.ojc31.seng440assignment2.databinding.PreferencesContainerViewBinding
import nz.ac.uclive.ojc31.seng440assignment2.fragments.SettingsFragment


@Composable
fun SettingsScreen() {
    AndroidViewBinding(PreferencesContainerViewBinding::inflate) {
        //val settingsFragment = fragmentContainerView.findFragment<SettingsFragment>()
    }
}