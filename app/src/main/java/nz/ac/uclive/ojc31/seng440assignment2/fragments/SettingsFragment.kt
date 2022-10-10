package nz.ac.uclive.ojc31.seng440assignment2.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import nz.ac.uclive.ojc31.seng440assignment2.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val viewModel: SettingsViewModel by activityViewModels()
        val sharedPreferences = preferenceManager.sharedPreferences


        val listener = SharedPreferences.OnSharedPreferenceChangeListener() {prefs, key ->
            when (key) {
                "app_theme" -> viewModel.appTheme.value = prefs.getString(key, "system_default")!!
            }
        }
        sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)

    }
}