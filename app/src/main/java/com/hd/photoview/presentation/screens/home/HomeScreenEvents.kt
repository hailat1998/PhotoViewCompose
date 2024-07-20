package com.hd.photoview.presentation.screens.home

sealed class HomeScreenEvents {
    data object LoadPhoto : HomeScreenEvents()
    data class SearchPhoto(val query: String) : HomeScreenEvents()
    data object ChangeQuality : HomeScreenEvents()

    data class Download(val url: String) : HomeScreenEvents()
}