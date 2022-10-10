package nz.ac.uclive.ojc31.seng440assignment2.fragments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    val appTheme = mutableStateOf("system_default")


}