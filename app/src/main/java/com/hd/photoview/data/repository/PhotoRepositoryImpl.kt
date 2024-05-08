package com.hd.photoview.data.repository

import android.util.Log
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.Photos
import com.hd.photoview.data.remote.dto.Result
import com.hd.photoview.data.remote.dto.UnsplashApi
import com.hd.photoview.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(val unsplashApi : UnsplashApi) : PhotoRepository {
    override suspend fun getPhotos(): Flow<Resources<Photos>> {
        Log.i("Images" , "called get photo")
      return flow{

          emit(Resources.Loading(true))

          val remoteData = try {
              unsplashApi.fetchPhotos()
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
}
