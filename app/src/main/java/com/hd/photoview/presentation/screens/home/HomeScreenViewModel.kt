package com.hd.photoview.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hd.photoview.core.di.IoDispatcher
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor( private val photoRepository: PhotoRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher) : ViewModel() {


  private val  _state = MutableStateFlow(HomeScreenState())
    val state get() = _state.asStateFlow()


    var photoList: Flow<PagingData<PhotoItem>> = photoRepository.getPhotosPaging()
                                                   .cachedIn(viewModelScope)
    var searchPhotoList: Flow<PagingData<PhotoItem>> = photoRepository.getPhotosPaging()
                                                         .cachedIn(viewModelScope)

    fun onEvents(event : HomeScreenEvents){
        when(event){
            is HomeScreenEvents.LoadPhoto ->{
             photoList = loadWithPaging()
                         .cachedIn(viewModelScope)
            }
           is HomeScreenEvents.SearchPhoto -> {
              searchPhotoList = searchWithPaging(event.query)
                                .cachedIn(viewModelScope)
            }
            is HomeScreenEvents.Download -> {
                downloadPhoto(event.url)
            }
            else -> {}
        }
    }

    init {
        loadWithPaging()
    }

    private fun load(){
        viewModelScope.launch(coroutineDispatcher) {
            photoRepository.getPhotos().collect{
                when(it){
                   is Resources.Error -> {
                       _state.value = _state.value.copy(isLoading = false)
                   }
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isLoading = it.isLoading)
                    }
                    is Resources.Success -> {
                       it.data?.let{  photos ->
                           _state.value = _state.value.copy(listPhoto = it.data)
                       }
                    }
                }
            }
        }
    }

    private fun searchPhoto(query: String){
     viewModelScope.launch(coroutineDispatcher) {
         photoRepository.searchPhoto(query).collect{
             when(it){
                 is Resources.Error -> {
                     _state.value = _state.value.copy(isLoading = false)
                 }
                 is Resources.Loading -> {
                     _state.value = _state.value.copy(isLoading = it.isLoading)
                 }
                 is Resources.Success -> {
                     it.data?.let{  photos ->
                         _state.value = _state.value.copy(listPhoto = photos.photoItems)
                     }
                 }
             }
         }
       }
    }

    private fun downloadPhoto(url: String){
        photoRepository.enqueueDownload(url)
    }

    private fun loadWithPaging() = photoRepository.getPhotosPaging()
    private fun searchWithPaging(query: String) = photoRepository.searchPhotoPaging(query)
    }