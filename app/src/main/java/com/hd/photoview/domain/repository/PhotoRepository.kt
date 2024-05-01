package com.hd.photoview.domain.repository

import com.hd.photoview.core.utils.Resources
import com.hd1998.photofetch.api.Photos
import kotlinx.coroutines.flow.Flow
import com.hd1998.photofetch.api.Result

interface PhotoRepository {
    suspend fun getPhotos():Flow<Resources<Photos>>

    fun searchPhoto(query : String): Flow<Resources<Result>>

}