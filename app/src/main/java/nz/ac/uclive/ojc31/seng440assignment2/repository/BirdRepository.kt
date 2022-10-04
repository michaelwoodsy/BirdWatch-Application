package nz.ac.uclive.ojc31.seng440assignment2.repository

import dagger.hilt.android.scopes.ActivityScoped

import nz.ac.uclive.ojc31.seng440assignment2.data.BirdApi
import nz.ac.uclive.ojc31.seng440assignment2.data.Birds
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@ActivityScoped
class BirdRepository @Inject constructor(
    private val api: BirdApi
){

    suspend fun getBirdList(): Resource<Birds> {
        val response = try {
            api.getBirdList(
                "species",
                "json"
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }


    suspend fun getBirdInfo(bird: String): Resource<Birds> {
        val response = try {
            api.getBirdInfo(
                "species",
                "json",
                bird
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}