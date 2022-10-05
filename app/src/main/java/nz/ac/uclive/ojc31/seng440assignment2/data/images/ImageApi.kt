package nz.ac.uclive.ojc31.seng440assignment2.data.images

import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("rest/")
    suspend fun getBirdImages(
        @Query("method") method: String,
        @Query("api_key") api_key: String,
        @Query("text") birdName: String,
        @Query("sort") sort: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: Int,
    ): Images
}