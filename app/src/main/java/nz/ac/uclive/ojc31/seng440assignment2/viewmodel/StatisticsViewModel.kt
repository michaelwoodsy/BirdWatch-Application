package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.repository.StatisticsRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {
    var entryCount = mutableStateOf("")
    var achievementsCount = mutableStateOf("")
    var mostRecentEntry = mutableStateOf("")
    var oldestEntry = mutableStateOf("")
    var mostCommonBird = mutableStateOf("")

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            statisticsRepository.getEntryCount().collect { count ->
                entryCount.value = count.toList()[0].toString()
            }
        }
        viewModelScope.launch {
            statisticsRepository.getAchievementsCount().collect { count ->
                achievementsCount.value = count.toList()[0].toString()
            }
        }
        viewModelScope.launch {
            statisticsRepository.getMostRecentEntry().collect { date ->
                if (date.isEmpty()) {
                    mostRecentEntry.value = "No recent entry"
                } else {
                    mostRecentEntry.value = date.toList()[0].toString()
                }
            }
        }

        viewModelScope.launch {
            statisticsRepository.getFirstEntry().collect { date ->
                if (date.isEmpty()) {
                    oldestEntry.value = "No oldest entry"
                } else {
                    oldestEntry.value = date.toList()[0].toString()
                }
            }
        }

        viewModelScope.launch {
            statisticsRepository.getMostCommonBird().collect { bird ->
                if (bird.isEmpty()) {
                    mostCommonBird.value = "No common bird"
                } else {
                    mostCommonBird.value = bird.toList()[0]
                }
            }
        }
    }

    fun isLoading(): Boolean {
        var isLoading = false

        if (entryCount.value == "" || achievementsCount.value == "" || mostRecentEntry.value == "" || oldestEntry.value == "") {
            isLoading = true
        }

        return isLoading
    }
}