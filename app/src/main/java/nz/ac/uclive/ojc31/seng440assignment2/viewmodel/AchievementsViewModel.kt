package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.model.Achievement
import nz.ac.uclive.ojc31.seng440assignment2.repository.AchievementRepository
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository
) : ViewModel() {
    var achievementList = mutableStateOf<List<Achievement>>(listOf())
    var isLoading = mutableStateOf(false)

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            isLoading.value = true

            achievementRepository.getAllAchievements().collect { achievement ->
                if (achievement.isEmpty()) {
                    isLoading.value = false
                    return@collect
                }

                achievementList.value = achievement.toList()
                isLoading.value = false
            }
        }
    }
}