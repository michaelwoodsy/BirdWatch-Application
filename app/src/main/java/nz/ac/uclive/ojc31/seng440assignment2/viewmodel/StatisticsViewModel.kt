package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.StatisticsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val birdRepository: BirdRepository,
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
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    mostRecentEntry.value = date.toList()[0].format(formatter)
                }
            }
        }

        viewModelScope.launch {
            statisticsRepository.getFirstEntry().collect { date ->
                if (date.isEmpty()) {
                    oldestEntry.value = "No oldest entry"
                } else {
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    oldestEntry.value = date.toList()[0].format(formatter)
                }
            }
        }

        viewModelScope.launch {
            statisticsRepository.getMostCommonBird().collect { bird ->
                if (bird.isNullOrEmpty()) {
                    mostCommonBird.value = "No common bird"
                } else {
                    val birdInfo = birdRepository.getBirdInfo(bird.toList()[0])
                    mostCommonBird.value = birdInfo.data!![0].comName
                }
            }
        }
    }

    fun isLoading(): Boolean {
        var isLoading = false

        if (entryCount.value == "" || achievementsCount.value == "" || mostRecentEntry.value == ""
            || oldestEntry.value == "" || mostCommonBird.value == "") {
            isLoading = true
        }

        return isLoading
    }
}