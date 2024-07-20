package com.hd.photoview.domain.repository

import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.Photos
import com.hd.photoview.data.remote.dto.Result
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getPhotos():Flow<Resources<Photos>>

    fun searchPhoto(query : String): Flow<Resources<Result>>


    fun enqueueDownload(url: String)
}