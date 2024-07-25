package com.hd.photoview.data.remote.dto
import com.hd.photoview.data.remote.dto.Photos
import retrofit2.http.GET
import retrofit2.http.Query


interface UnsplashApi {
    @GET("/photos/?client_id=3b6eNighxPn-ABVRnc7P1ZrlguRVwZiZmmfEm9mjcXU")
    suspend fun fetchPhotos(): List<PhotoItem>

    @GET("/search/photos/?client_id=3b6eNighxPn-ABVRnc7P1ZrlguRVwZiZmmfEm9mjcXU")
    suspend fun searchPhoto(@Query("query") query : String? ) : Result

}