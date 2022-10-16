package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry
import nz.ac.uclive.ojc31.seng440assignment2.repository.EntryRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: EntryRepository
) : ViewModel(){
    suspend fun saveEntry(ctx: Context) {
        saving.value = true

        viewModelScope.launch {
            val entry = Entry(
                speciesCode = currentBirdCode.value,
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
        if (currentBirdCode.value != "" && datePicked.value != "" && currentRegion.value != "") {
            return true
        }
        return false
    }

    fun initFromArguments(ctx: Context, birdId: String, birdName: String, lat: String, long: String) {
        if (birdId.isNotEmpty()) this.currentBirdCode.value = birdId
        if (birdName.isNotEmpty()) this.currentBirdName.value = birdName

        if (lat.isNotEmpty() && long.isNotEmpty()) {
            this.currentLat.value = lat.toDouble()
            this.currentLong.value = long.toDouble()
            val geoCoder = Geocoder(ctx)
            viewModelScope.launch {
                val addressList = geoCoder.getFromLocation(currentLat.value, currentLong.value, 1)
                currentRegion.value = if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    address.locality ?: address.subAdminArea ?: address.adminArea ?: "Latitude: $lat, Longitude: $long"
                } else ""
            }
        }
    }

    val cal = Calendar.getInstance()

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    var currentBirdCode = mutableStateOf("")
    var currentBirdName = mutableStateOf("")
    var datePicked = mutableStateOf("$day/${month + 1}/$year")
    var currentRegion = mutableStateOf("")
    var currentLat = mutableStateOf(200.0)
    var currentLong = mutableStateOf(200.0)

    var imageId = mutableStateOf("")
    var saving = mutableStateOf(false)

}