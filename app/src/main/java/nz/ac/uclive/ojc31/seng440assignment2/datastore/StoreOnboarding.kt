package nz.ac.uclive.ojc31.seng440assignment2.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreOnboarding(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("OnboardingComplete")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val getOnboardingState: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETE] ?: false
        }

    suspend fun setOnboardingState(onboardingState: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE] = onboardingState
        }
    }
}