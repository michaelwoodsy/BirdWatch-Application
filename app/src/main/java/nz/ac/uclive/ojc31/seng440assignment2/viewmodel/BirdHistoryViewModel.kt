package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.data.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.data.EntryDTO
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.EntryRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class BirdHistoryViewModel @Inject constructor (
    private val entryRepo: EntryRepository,
    private val birdRepo: BirdRepository
    ) : ViewModel() {

    var historyList = mutableStateOf<List<EntryDTO>>(listOf())
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")


    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            isLoading.value = true

            entryRepo.getAllFlow().collect() { history ->
                if (history.isEmpty()) {
                    isLoading.value = false
                    return@collect
                }
                val speciesQuery = history.joinToString(separator = ",") { it.speciesCode }

                when (val result = birdRepo.getBirdInfo(speciesQuery)) {
                    is Resource.Success -> {
                        loadError.value = ""

                        val birds = result.data!!
                        val birdsBySpeciesCode: Map<String, BirdsItem> =
                            birds.associateBy { it.speciesCode }

                        val entryDTOList = history.filter { birdsBySpeciesCode[it.speciesCode] != null }
                            .map { entry ->
                                birdsBySpeciesCode[entry.speciesCode]?.let { bird ->
                                    EntryDTO(bird, entry.observedDate, entry.observedLocation)
                                }
                            }
                        historyList.value = entryDTOList as List<EntryDTO>
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
}