package com.hd.photoview.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val photoRepository: PhotoRepository): ViewModel() {

    lateinit var searchPhotoList: Flow<PagingData<Photo>>

    fun onEvents(event: SearchEvent) {
        when(event) {
            is SearchEvent.SearchPhoto -> {
                searchPhotoList = searchWithPaging(event.query)
                    .cachedIn(viewModelScope)
            }
        }
    }

    private fun searchWithPaging(query: String) = photoRepository.searchPhotoPaging(query)
}