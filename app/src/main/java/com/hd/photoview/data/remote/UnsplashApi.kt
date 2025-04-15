package com.hd.photoview.data.remote


import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/photos/?")
    suspend fun fetchPhotos(@Query("page") page: Int): List<PhotoItem>

    @GET("/search/photos/?")
    suspend fun searchPhoto(
        @Query("query") query : String?,
        @Query("page") page: Int ) : Result

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): List<PhotoItem>

}