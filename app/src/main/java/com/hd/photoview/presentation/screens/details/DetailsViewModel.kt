package com.hd.photoview.presentation.screens.details

import androidx.lifecycle.ViewModel
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(private val photoRepository: PhotoRepository): ViewModel() {

    fun onEvent(event: DetailsEvent) {

        when(event) {
         is  DetailsEvent.Download -> downloadPhoto(event.photo, event.selected)
        }

    }

    private fun downloadPhoto(photo: Photo, selectedQ: String){
        photoRepository.enqueueDownload(photo , selectedQ)
    }

}