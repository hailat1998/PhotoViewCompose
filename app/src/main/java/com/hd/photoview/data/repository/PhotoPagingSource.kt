package com.hd.photoview.data.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.hd.photoview.domain.model.Photo
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hd.photoview.data.remote.UnsplashApi
import com.hd.photoview.data.remote.dto.dto.mapper.toPhoto
import dagger.Component.Factory
import java.io.IOException
import javax.inject.Inject




class PhotoPagingSource @Inject constructor(private val unsplashApi: UnsplashApi,
                                            private val query: String = "",
                                            private val  username: String = ""):
    PagingSource<Int , Photo>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try{
            val nextPageNumber = params.key ?: 1
            val response: List<Photo> = if (query.isNotEmpty()) {
                unsplashApi.searchPhoto(query, nextPageNumber).photoItems.map { it.toPhoto() }
            } else if (username.isNotEmpty()) {
                unsplashApi.getUserPhotos(username, nextPageNumber).map { it.toPhoto() }
            } else {
                unsplashApi.fetchPhotos(nextPageNumber).map { it.toPhoto() }
            }

            Log.i("PAGING", "load called")

            LoadResult.Page(
                data = response,
                prevKey = if(nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = nextPageNumber + 1
            )
        } catch (e: IOException) {
            Log.e("PAGING", "Network error: ${e.message}")
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("PAGING", "HTTP error: ${e.message}")
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("PAGING", "Unknown error: ${e.message}")
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}
