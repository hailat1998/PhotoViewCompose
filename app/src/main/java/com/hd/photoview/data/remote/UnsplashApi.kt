package com.hd.photoview.data.remote


import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Result
import retrofit2.http.GET
import retrofit2.http.Query
import com.hd.photoview.API_KEY


interface UnsplashApi {
    @GET("/photos/?client_id=${API_KEY}")
    suspend fun fetchPhotos(@Query("page") page: Int): List<PhotoItem>

    @GET("/search/photos/?client_id=${API_KEY}")
    suspend fun searchPhoto(@Query("query") query : String? ) : Result

}