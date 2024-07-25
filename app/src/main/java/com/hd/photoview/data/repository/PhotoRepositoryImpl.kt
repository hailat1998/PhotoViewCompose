package com.hd.photoview.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Photos
import com.hd.photoview.data.remote.dto.Result
import com.hd.photoview.data.remote.dto.UnsplashApi
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(private val unsplashApi : UnsplashApi,
                                              @ApplicationContext private val context: Context ) : PhotoRepository {
    override suspend fun getPhotos(): Flow<Resources<List<PhotoItem>>> {
        Log.i("Images" , "called get photo")
      return flow{

          emit(Resources.Loading(true))

          val remoteData = try {
            val k =  unsplashApi.fetchPhotos()

              Log.d("Photos", k.toString())
              k
          }catch(e: Exception){
              emit(Resources.Error("Couldn't load any photo"))
              println(e)
              null
          }
          if(remoteData == null){
              emit(Resources.Loading(false))
          }
          remoteData.let{
              emit(Resources.Success(data = it))
              println(it)
              Log.i("From Data", "$it")
              emit(Resources.Loading(false))
          }
       }
    }

    override fun searchPhoto(query : String): Flow<Resources<Result>> {
        return flow{
            emit(Resources.Loading(true))
            val remoteData = try {
                unsplashApi.searchPhoto(query)
            }catch(e: Exception){
                emit(Resources.Error("Couldn't load any photo"))
                println(e)
                null
            }
            if(remoteData == null){
                emit(Resources.Loading(false))
            }
            remoteData.let{
                emit(Resources.Success(data = it))
                println(it)
                Log.i("From Data", "$it")
                emit(Resources.Loading(false))
            }
        }
    }

    override fun enqueueDownload(url: String) {
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Downloading Photo")
            setDescription("Photo is being downloaded from Unsplash")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.zip")
            setAllowedOverMetered(true)
            setAllowedOverRoaming(false)
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
