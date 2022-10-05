package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.util.Xml
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class BirdDetailViewModel @Inject constructor(
    private val repository: BirdRepository
) : ViewModel() {
    var birdImageUrl = mutableStateOf("")

    suspend fun getBirdInfo(birdName: String): Resource<Birds> {
        val birdInfo = repository.getBirdInfo(birdName)
        when (birdInfo) {
            is Resource.Success -> {
                val birdData = birdInfo.data!!
                val birdName = birdData[0].comName
                loadBirdImages(birdName)
            }
            else -> {}
        }
        return birdInfo
    }

    private suspend fun loadBirdImages(birdName: String) {
        val imageResponse = repository.getBirdImages(birdName)
        val photos = imageResponse.data!!.photos
        val imageUrl = ""
        if (photos.photo.isNotEmpty()) {
            val bestPhoto = photos.photo[0]
            val photoId = bestPhoto.id
            val serverId = bestPhoto.server
            val secret = bestPhoto.secret
            birdImageUrl.value = "https://live.staticflickr.com/${serverId}/${photoId}_${secret}.jpg"
        } else {
            birdImageUrl.value = "https://www.justcolor.net/kids/wp-content/uploads/sites/12/nggallery/birds/coloring-pages-for-children-birds-82448.jpg"
        }
    }
}