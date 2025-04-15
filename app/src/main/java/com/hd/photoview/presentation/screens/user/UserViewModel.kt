package com.hd.photoview.presentation.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewScoped
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor( private val photoRepository: PhotoRepository): ViewModel() {
    fun userPhotos(username: String) = photoRepository.userPhotosPaging(username).cachedIn(viewModelScope)
}