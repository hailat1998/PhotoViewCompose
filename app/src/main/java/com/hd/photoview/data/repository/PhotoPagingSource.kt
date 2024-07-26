package com.hd.photoview.data.repository

import android.provider.ContactsContract.Contacts.Photo
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.data.remote.dto.UnsplashApi
import javax.inject.Inject

class PhotoPagingSource @Inject constructor(private val unsplashApi: UnsplashApi , private val query: String = ""):
    PagingSource<Int , PhotoItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
       return try{
            val nextPageNumber = params.key ?: 1
            val response  = if (query.isEmpty())unsplashApi.fetchPhotos(nextPageNumber) else unsplashApi.searchPhoto(query).photoItems
             LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        }catch(e : Exception){
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
