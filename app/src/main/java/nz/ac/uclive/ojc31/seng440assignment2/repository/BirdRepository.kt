package nz.ac.uclive.ojc31.seng440assignment2.repository

import dagger.hilt.android.scopes.ActivityScoped

import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdApi
import nz.ac.uclive.ojc31.seng440assignment2.data.birds.Birds
import nz.ac.uclive.ojc31.seng440assignment2.data.images.ImageApi
import nz.ac.uclive.ojc31.seng440assignment2.data.images.Images
import nz.ac.uclive.ojc31.seng440assignment2.util.Resource
import javax.inject.Inject

@ActivityScoped
class BirdRepository @Inject constructor(
    private val birdApi: BirdApi,
    private val imageApi: ImageApi
){

    suspend fun getBirdList(): Resource<Birds> {
        val response = try {
            birdApi.getBirdList(
                "species",
                "json"
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }


    suspend fun getBirdInfo(birdCodes: String): Resource<Birds> {
        val response = try {
            birdApi.getBirdInfo(
                "json",
                birdCodes
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getBirdImages(birdName: String): Resource<Images> {
        val response = try {
            imageApi.getBirdImages(
                "flickr.photos.search",
                "d310fa96de3a14787ecc43d28aaf1ad2",
                birdName,
                "relevance",
                5,
                1,
                "json",
                1
            )
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }
        return Resource.Success(response)
    }
}