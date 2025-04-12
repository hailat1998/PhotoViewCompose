package com.hd.photoview.data.repository

import android.util.Log
import com.hd.photoview.domain.model.Photo
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hd.photoview.data.remote.UnsplashApi
import com.hd.photoview.data.remote.dto.dto.mapper.toPhoto
import javax.inject.Inject

class PhotoPagingSource @Inject constructor(private val unsplashApi: UnsplashApi, private val query: String = ""):
    PagingSource<Int , Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
       return try{
            val nextPageNumber = params.key ?: 1
            val response  = if (query.isEmpty())unsplashApi.fetchPhotos(nextPageNumber).map{it.toPhoto()} else unsplashApi.searchPhoto(query, nextPageNumber).photoItems.map{it.toPhoto()}

           Log.i("PAGING", "load called")

             LoadResult.Page(
                data = response,
                prevKey = if(nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = nextPageNumber + 1
            )
        }catch(e : Exception){
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
