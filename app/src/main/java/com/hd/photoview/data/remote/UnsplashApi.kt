package com.hd1998.photofetch.api
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY="3b6eNighxPn-ABVRnc7P1ZrlguRVwZiZmmfEm9mjcXU"
interface UnsplashApi {
    @GET("photos/?")
    suspend fun fetchPhotos(): List<PhotoItem>
    @GET("search/photos/?")
    suspend fun searchPhoto(@Query("query") query : String? ) : com.hd1998.photofetch.api.Result

}