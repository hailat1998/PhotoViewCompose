package com.hd.photoview.presentation.screens.home

import com.hd.photoview.domain.model.Photo

sealed class HomeScreenEvents {
    data object LoadPhoto : HomeScreenEvents()
    data class SearchPhoto(val query: String) : HomeScreenEvents()
    data object ChangeQuality : HomeScreenEvents()

    data class Download(val photo: Photo , val selected: String) : HomeScreenEvents()
}