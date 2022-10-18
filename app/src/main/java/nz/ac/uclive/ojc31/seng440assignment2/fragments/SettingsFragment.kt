package nz.ac.uclive.ojc31.seng440assignment2.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import nz.ac.uclive.ojc31.seng440assignment2.R


class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (activity != null && isAdded) {

            val sharedPreferences = preferenceManager.sharedPreferences

            val listener = SharedPreferences.OnSharedPreferenceChangeListener() { prefs, key ->
                when (key) {
                    "app_theme" -> viewModel.appTheme.value =
                        prefs.getString(key, "system_default")!!
                }
            }
            sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}