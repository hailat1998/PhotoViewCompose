package com.hd.photoview.presentation.screens.details

import com.hd.photoview.domain.model.Photo

sealed class DetailsEvent {

    data class Download(val photo: Photo, val selected: String) : DetailsEvent()

}