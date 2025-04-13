package com.hd.photoview.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hd.photoview.core.utils.DownloadManagerHelper
import com.hd.photoview.data.remote.UnsplashApi
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(private val unsplashApi : UnsplashApi,
                                              @ApplicationContext private val context: Context,
                                             ) : PhotoRepository {



                                                  override fun getPhotosPaging(): Flow<PagingData<Photo>>  = Pager(
                                                      config = PagingConfig(pageSize = 30,
                                                          enablePlaceholders = true,
                                                          prefetchDistance = 5),
                                                      pagingSourceFactory = { PhotoPagingSource(unsplashApi) }
                                                  ).flow


                                                 override fun searchPhotoPaging(query: String): Flow<PagingData<Photo>>  = Pager(
                                                     config = PagingConfig(pageSize = 30,
                                                         enablePlaceholders = true,
                                                         prefetchDistance = 5),
                                                     pagingSourceFactory = { PhotoPagingSource(unsplashApi , query) }
                                                 ).flow

    override fun enqueueDownload(photo: Photo, selected: String) {

        val url = when(selected) {
            "full" -> photo.full
            "small" -> photo.small
            else -> photo.regular
        }

        val downloadHelper = DownloadManagerHelper(context)
        downloadHelper.downloadFile(url, photo)
    }
}
