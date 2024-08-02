package com.hd.photoview.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.Result
import com.hd.photoview.data.remote.dto.UnsplashApi
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(private val unsplashApi : UnsplashApi,
                                              @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient) : PhotoRepository {


           override fun getPhotosPaging(): Flow<PagingData<Photo>>  = Pager(
                    config = PagingConfig(pageSize = 10 , maxSize = 30),
                 pagingSourceFactory = { PhotoPagingSource(unsplashApi) }
             ).flow

           override fun searchPhotoPaging(query: String): Flow<PagingData<Photo>>  = Pager(
               config = PagingConfig(pageSize = 10 , maxSize = 30),
               pagingSourceFactory = { PhotoPagingSource(unsplashApi , query) }
           ).flow





    override suspend fun getPhotos(): Flow<Resources<List<PhotoItem>>> {
        Log.i("Images" , "called get photo")
      return flow{

          emit(Resources.Loading(true))

          val remoteData = try {

            val k =  unsplashApi.fetchPhotos(1)

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

    override fun enqueueDownload(photo: Photo, selected: String) {
        val url = when(selected){
            "full" -> photo.full
            "small" -> photo.small
            else -> photo.regular
        }

        val requestW = Request.Builder()
            .url(url)
            .build()



            okHttpClient.newCall(requestW).enqueue(object : okhttp3.Callback {


                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: okhttp3.Call, response: Response) {
                    response.use {
                        if (!it.isSuccessful) throw IOException("Unexpected code $response")


                        val responseBody = response.body?.string()
                        val document = Jsoup.parse(responseBody)
                        val title = document.title()


                        val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9\\.\\-]"), "_")


                        val downloadRequest = DownloadManager.Request(Uri.parse(url)).apply {
                            setTitle("Downloading $sanitizedTitle")
                            setDescription("File is being downloaded")
                            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                if(sanitizedTitle.isNotEmpty())"$sanitizedTitle.jpg" else "unsplash_pucture.jpg")
                            setMimeType("image/jpeg")
                                .
                            setAllowedOverMetered(true)
                            setAllowedOverRoaming(false)
                        }

                        // Get the DownloadManager
                        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManager.enqueue(downloadRequest)
                    }
                }
            }
            )
    }
}
