package com.hd.photoview.domain.repository

import androidx.paging.PagingData
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Photos
import com.hd.photoview.data.remote.dto.Result
import com.hd.photoview.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
  //  suspend fun getPhotos():Flow<Resources<List<PhotoItem>>>
    fun getPhotosPaging(): Flow<PagingData<Photo>>
   // fun searchPhoto(query : String): Flow<Resources<Result>>
    fun searchPhotoPaging(query: String): Flow<PagingData<Photo>>
    fun enqueueDownload(photo: Photo, selected: String)
    fun userPhotosPaging(usename: String): Flow<PagingData<Photo>>
}