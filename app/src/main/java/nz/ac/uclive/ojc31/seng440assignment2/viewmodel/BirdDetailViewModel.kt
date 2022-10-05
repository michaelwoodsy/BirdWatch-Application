package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.data.images.Images
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class BirdDetailViewModel @Inject constructor(
    private val repository: BirdRepository
) : ViewModel() {

    suspend fun getBirdInfo(birdName: String): Resource<Birds> {
        return repository.getBirdInfo(birdName)
    }

    suspend fun getBirdImage(birdName: String): Resource<Images> {
        return repository.getBirdImages(birdName)
    }
}