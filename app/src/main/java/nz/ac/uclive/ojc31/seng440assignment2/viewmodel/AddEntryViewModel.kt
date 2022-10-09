package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.repository.EntryRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: EntryRepository,
    private val birdRepository: BirdRepository,
) : ViewModel(){
    suspend fun saveEntry(ctx: Context) {
        val birdInfoDeferred = viewModelScope.async {
            birdRepository.getBirdInfo(currentBirdId.value)
        }
        val birdInfo = birdInfoDeferred.await()

        viewModelScope.launch {
            val birdsItem = birdInfo.data!![0]
            val entry = Entry(
                speciesCode = birdsItem.speciesCode,
                observedDate = datePicked.value,
                observedLocation = currentRegion.value,
                observedLat = currentLat.value,
                observedLong = currentLong.value,
                imageId = imageId.value.toIntOrNull(),
            )
            repository.insert(entry = entry)
            Toast.makeText(ctx, "Successfully Saved Entry!", Toast.LENGTH_SHORT).show()
        }

    }

    fun canSave(): Boolean {
        if (currentBirdId.value != "" && datePicked.value != "" && currentRegion.value != "") {
            return true
        }
        return false
    }


    val cal = Calendar.getInstance()

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    var currentBirdId = mutableStateOf("")
    var currentBirdName = mutableStateOf("")
    var datePicked = mutableStateOf("$day/${month + 1}/$year")
    var currentRegion = mutableStateOf("")
    var currentLat = mutableStateOf(200.0)
    var currentLong = mutableStateOf(200.0)

    var imageId = mutableStateOf("")

}