package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.data.challenges.ChallengeDTO
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.ChallengeRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class ChallengeListViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val birdRepository: BirdRepository
) : ViewModel() {

    //val challenges
    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf("")
    var challenges = mutableStateListOf<ChallengeDTO>()

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        isLoading.value = true
        challenges.clear()
        viewModelScope.launch {
            challengeRepository.getAllFlow().collect { challengeList ->
                if (challengeList.isEmpty()) {
                    isLoading.value = false
                    return@collect
                }

                val speciesQuery = challengeList.joinToString(separator = ",") {it.speciesCode}

                when (val result = birdRepository.getBirdInfo(speciesQuery)) {
                    is Resource.Success -> {
                        loadError.value = ""
                        mapChallenges(challengeList, result.data!!)
                        isLoading.value = false
                    }
                    is Resource.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }
                    else -> {}
                }
            }
        }
    }

    private fun mapChallenges(challengeList: List<Challenge>, birds: Birds) {
        val birdBySpeciesCode = birds.associateBy { it.speciesCode }

        challengeList.filter { birdBySpeciesCode[it.speciesCode] != null }
            .forEach { challenge ->
                birdBySpeciesCode[challenge.speciesCode]?.let { bird ->
                    challenges.add(ChallengeDTO(challenge, bird))
                }
            }
    }

    fun completeChallenge(index: Int) {

    }
}