package nz.ac.uclive.ojc31.seng440assignment2.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class BirdListViewModel @Inject constructor(
    private val repository: BirdRepository,
) : ViewModel() {

    private var curPage = 0

    var searchText = mutableStateOf("")

    var birdList = mutableStateOf<List<BirdsItem>>(listOf())
    private var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedBirdList = listOf<BirdsItem>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadBirds()
    }

    fun searchBirdList(query: String) {
        val listToSearch = if(isSearchStarting) {
            birdList.value
        } else {
            cachedBirdList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                birdList.value = cachedBirdList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.comName.contains(query.trim(), ignoreCase = true) ||
                        it.sciName.contains(query.trim(), ignoreCase = true)
            }
            if(isSearchStarting) {
                cachedBirdList = birdList.value
                isSearchStarting = false
            }
            birdList.value = results
            isSearching.value = true
        }
    }

    fun loadBirds() {
        viewModelScope.launch {
            isLoading.value = true
            when(val result = repository.getBirdList()) {
                is Resource.Success -> {
                    endReached.value = true
                    val birdEntries = result.data!!

                    curPage++

                    loadError.value = ""
                    isLoading.value = false

                    birdList.value += birdEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

//    fun loadBirdImages() {
//        for (bird in birdList.value) {
//            viewModelScope.launch {
//                when(val result = repository.getBirdImages(bird.comName)) {
//                    is Resource.Success -> {
//                        val birdImages = result.data!!
//                    }
//                    is Resource.Error -> {
//                    }
//                    else -> {}
//                }
//            }
//        }
//    }
//
//
//    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
//        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
//
//        Palette.from(bmp).generate { palette ->
//            palette?.dominantSwatch?.rgb?.let { colorValue ->
//                onFinish(Color(colorValue))
//            }
//        }
//    }
}