package nz.ac.uclive.ojc31.seng440assignment2.data

import nz.ac.uclive.ojc31.seng440assignment2.util.Constants.EBIRD_API_TOKEN
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BirdApi {
    @Headers("X-eBirdApiToken: $EBIRD_API_TOKEN")
    @GET("ref/taxonomy/ebird")
    suspend fun getBirdList(
        @Query("cat") category: String,
        @Query("fmt") format: String,
    ): Birds

    @Headers("X-eBirdApiToken: $EBIRD_API_TOKEN")
    @GET("ref/taxonomy/ebird")
    suspend fun getBirdInfo(
        @Query("species") species: String,
        @Query("fmt") format: String
    ):  Birds
}