package nz.ac.uclive.ojc31.seng440assignment2.repository

import dagger.hilt.android.scopes.ActivityScoped

import nz.ac.uclive.ojc31.seng440assignment2.data.BirdApi
import nz.ac.uclive.ojc31.seng440assignment2.util.Constants.API_TOKEN
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@ActivityScoped
class BirdRepository @Inject constructor(
    private val api: BirdApi
){

    suspend fun getBirdList(): Resource<String> {
        val response = try {
            api.getBirdList()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }


    suspend fun getBirdInfo(bird: String): Resource<String> {
        val response = try {
            api.getBirdInfo(bird)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}