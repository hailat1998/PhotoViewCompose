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

    fun searchPhotoList(query: String) =  photoRepository.searchPhotoPaging(query).cachedIn(viewModelScope)

    fun onEvents(event: SearchEvent) {
        when(event) {
            is SearchEvent.SearchPhoto -> {

            }
        }
    }
}