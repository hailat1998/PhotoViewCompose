package com.hd.photoview.domain.repository

import androidx.paging.PagingData
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Photos
import com.hd.photoview.data.remote.dto.Result
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getPhotos():Flow<Resources<List<PhotoItem>>>
    fun getPhotosPaging(): Flow<PagingData<PhotoItem>>
    fun searchPhoto(query : String): Flow<Resources<Result>>
    fun searchPhotoPaging(query: String): Flow<PagingData<PhotoItem>>
    fun enqueueDownload(url: String)
}