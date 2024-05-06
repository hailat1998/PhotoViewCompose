package com.hd.photoview.presentation.screens

sealed class HomeScreenEvents {
    data object LoadPhoto : HomeScreenEvents()
    data object SearchPhoto : HomeScreenEvents()
    data object ChangeQuality : HomeScreenEvents()

    data object Download : HomeScreenEvents()
}