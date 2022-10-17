package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.graphs.SubScreen
import nz.ac.uclive.ojc31.seng440assignment2.model.Achievement
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
import nz.ac.uclive.ojc31.seng440assignment2.repository.AchievementRepository
import nz.ac.uclive.ojc31.seng440assignment2.notification.WeeklyNotificationService
import nz.ac.uclive.ojc31.seng440assignment2.repository.ChallengeRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.EntryRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: EntryRepository,
    private val challengeRepo: ChallengeRepository,
    private val achievementRepository: AchievementRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val cal = Calendar.getInstance()

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    var currentBirdCode = mutableStateOf("")
    var currentBirdName = mutableStateOf("")
    var birdFromArgs = mutableStateOf(false)
    var fromChallengeId : Long? = null

    var datePicked = mutableStateOf("$day/${month + 1}/$year")

    var currentRegion = mutableStateOf("")
    var currentLat = mutableStateOf(200.0)
    var currentLong = mutableStateOf(200.0)

    var imageId = mutableStateOf("")
    var saving = mutableStateOf(false)



    suspend fun saveEntry(ctx: Context) {
        saving.value = true
        val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

        val service = WeeklyNotificationService(ctx)

        viewModelScope.launch {
            val entry = Entry(
                speciesCode = currentBirdCode.value,
                observedDate = LocalDate.parse(datePicked.value, dateFormatter),
                observedLocation = currentRegion.value,
                observedLat = currentLat.value,
                observedLong = currentLong.value,
                imageId = imageId.value.toIntOrNull(),
            )
            repository.insert(entry = entry)
            if (fromChallengeId != null) {
                challengeRepo.deleteById(fromChallengeId!!)
            }
            addNotification(service)
            Toast.makeText(ctx, "Successfully Saved Entry!", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun addNotification(service: WeeklyNotificationService) {
        val totalNumberAchievementList = listOf(1, 5, 10, 25, 50, 100, 250, 500, 1000)
        val speciesNumberAchievementList = listOf(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024)

        repository.getAllFlow().collect { entries ->
            if (entries.isNotEmpty()) {
                if (totalNumberAchievementList.contains(entries.size)) {
                    if (entries.size == 1) {
                        val achievement = Achievement(
                            achievementCount = entries.size,
                            achievementType = "Entry Number",
                            receivedDate = LocalDate.now(),
                            achievementText = "You made your ${entries.size}st entry"
                        )
                        achievementRepository.insert(achievement = achievement)
                    } else {
                        val achievement = Achievement(
                            achievementCount = entries.size,
                            achievementType = "Entry Number",
                            receivedDate = LocalDate.now(),
                            achievementText = "You made your ${entries.size}th entry"
                        )
                        achievementRepository.insert(achievement = achievement)
                    }
                    service.showNotification()
                }
                var uniqueSpeciesList = mutableListOf<String>()
                for (entry in entries) {
                    if (uniqueSpeciesList.contains(entry.speciesCode)) {
                    } else {
                        uniqueSpeciesList.add(entry.speciesCode)
                    }
                }
                if (speciesNumberAchievementList.contains(uniqueSpeciesList.size)) {
                    if ((uniqueSpeciesList.size == 2) || (uniqueSpeciesList.size == 32)) {
                        val achievement = Achievement(
                            achievementCount = uniqueSpeciesList.size,
                            achievementType = "Species Number",
                            receivedDate = LocalDate.now(),
                            achievementText = "You entered your ${uniqueSpeciesList.size}nd unique species of bird"
                        )
                        achievementRepository.insert(achievement = achievement)
                        service.showNotification()
                    } else {
                        val achievement = Achievement(
                            achievementCount = uniqueSpeciesList.size,
                            achievementType = "Species Number",
                            receivedDate = LocalDate.now(),
                            achievementText = "You entered your ${uniqueSpeciesList.size}th unique species of bird"
                        )
                        achievementRepository.insert(achievement = achievement)
                        service.showNotification()
                    }
                }
            }
        }
    }

    fun canSave(): Boolean {
        if (currentBirdCode.value != "" && datePicked.value != "" && currentRegion.value != "") {
            return true
        }
        return false
    }

    init {
        val birdId = savedStateHandle.get<String>(SubScreen.AddEntryDetails.birdId)
        val birdName = savedStateHandle.get<String>(SubScreen.AddEntryDetails.birdName)
        val lat = savedStateHandle.get<String>(SubScreen.AddEntryDetails.lat)
        val long = savedStateHandle.get<String>(SubScreen.AddEntryDetails.long)
        val challengeId = savedStateHandle.get<String>(SubScreen.AddEntryDetails.challengeId)

        if (!birdId.isNullOrEmpty() && !birdName.isNullOrEmpty()) {
            this.currentBirdCode.value = birdId
            this.currentBirdName.value = birdName
            this.birdFromArgs.value = true
        }

        if (!challengeId.isNullOrEmpty()) this.fromChallengeId = challengeId.toLongOrNull()

        if (!lat.isNullOrEmpty() && !long.isNullOrEmpty()) {
            this.currentLat.value = lat.toDouble()
            this.currentLong.value = long.toDouble()
            val geoCoder = Geocoder(application)
            viewModelScope.launch {
                val addressList = geoCoder.getFromLocation(currentLat.value, currentLong.value, 1)
                currentRegion.value = if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    address.locality ?: address.subAdminArea ?: address.adminArea ?: "Latitude: $lat, Longitude: $long"
                } else ""
            }
        }

    }
}