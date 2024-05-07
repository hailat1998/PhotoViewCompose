package com.hd.photoview.presentation.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd.photoview.core.utils.Resources
import com.hd.photoview.domain.repository.PhotoRepository
import com.hd1998.photofetch.api.PhotoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor( private val photoRepository: PhotoRepository) : ViewModel() {
  var  state by mutableStateOf(HomeScreenState())


    fun onEvents(event : HomeScreenEvents){
        when(event){
            is HomeScreenEvents.LoadPhoto ->{
                load()
            }
            else -> {}
        }
    }

    private fun load(){
        viewModelScope.launch {
            photoRepository.getPhotos().collect{
                when(it){
                   is Resources.Error -> {
                       state = state.copy(isLoading = false)
                   }
                    is Resources.Loading -> {
                        state = state.copy(isLoading = it.isLoading)
                    }
                    is Resources.Success -> {
                       it.data?.let{  photos ->
                           state = state.copy(listPhoto = photos.photoItems)
                       }
                    }
                }
            }
        }
    }

}