package com.hd.photoview.presentation.screens.home

import com.hd.photoview.domain.model.Photo

sealed class HomeScreenEvents {
    data object LoadPhoto : HomeScreenEvents()
}