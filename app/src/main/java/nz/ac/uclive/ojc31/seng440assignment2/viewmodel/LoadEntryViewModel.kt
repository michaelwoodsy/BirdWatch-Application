package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.ChallengeRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoadEntryViewModel @Inject constructor (
    private val birdRepository: BirdRepository,
    private val challengeRepository: ChallengeRepository
        ) : ViewModel() {

    val isLoading = mutableStateOf(true)
    val isSaving = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    val lat = mutableStateOf(0.0)
    val long = mutableStateOf(0.0)

    val bird = mutableStateOf<BirdsItem?>(null)

    fun loadEntry(birdId: String, lat: Double, long: Double) {
        isLoading.value = true
        this.lat.value = lat
        this.long.value = long

        viewModelScope.launch {
            errorMessage.value = ""
            when (val result = birdRepository.getBirdInfo(birdId)) {
                is Resource.Success -> {
                    bird.value = result.data!![0]
                    isLoading.value = false
                }
                is Resource.Error -> {
                    isLoading.value = false
                    errorMessage.value = result.message!!
                }
                else -> {}
            }
        }
    }

    suspend fun saveEntry(ctx: Context) {
        isSaving.value = true
        val challenge = Challenge(
            speciesCode = bird.value!!.speciesCode,
            receivedDate = LocalDate.now(),
            lastSeenLat = lat.value,
            lastSeenLong = long.value
        )
        viewModelScope.launch {
            challengeRepository.insert(challenge)
            Toast.makeText(ctx, "Successfully Created Challenge!", Toast.LENGTH_SHORT).show()
        }

    }
}