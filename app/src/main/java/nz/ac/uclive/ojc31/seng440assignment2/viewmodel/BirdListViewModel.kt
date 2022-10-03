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
import nz.ac.uclive.ojc31.seng440assignment2.data.BirdItem
import nz.ac.uclive.ojc31.seng440assignment2.repository.BirdRepository
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@HiltViewModel
class BirdListViewModel @Inject constructor(
    private val repository: BirdRepository
) : ViewModel() {

    private var curPage = 0

    var searchText = mutableStateOf("")

    var birdList = mutableStateOf<List<BirdItem>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedBirdList = listOf<BirdItem>()
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
                it.COMMON_NAME.contains(query.trim(), ignoreCase = true) ||
                        it.SCIENTIFIC_NAME.contains(query.trim(), ignoreCase = true)
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
                    val birdSplit = birdEntries.split("\n")
                    val birdSplitSize = birdSplit.size

                    for (i in birdSplit.indices) {
                        if ((i != 0) and (i != birdSplitSize - 1)) {
                            val birdValues = birdSplit[i].split(',')
                            val bird = BirdItem(
                                SCIENTIFIC_NAME = birdValues[0],
                                COMMON_NAME = birdValues[1],
                                SPECIES_CODE = birdValues[2],
                                CATEGORY = birdValues[3],
                                TAXON_ORDER = birdValues[4],
                                COM_NAME_CODES = birdValues[5],
                                SCI_NAME_CODES = birdValues[6],
                                BANDING_CODES = birdValues[7],
                                ORDER = birdValues[8],
                                FAMILY_COM_NAME = birdValues[9],
                                FAMILY_SCI_NAME = birdValues[10],
                                REPORT_AS = birdValues[11],
                                EXTINCT = birdValues[12],
                                EXTINCT_YEAR = birdValues[13],
                                FAMILY_CODE = birdValues[14]
                            )
                            birdList.value += bird
                        }
                    }
                    curPage++

                    loadError.value = ""
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

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}