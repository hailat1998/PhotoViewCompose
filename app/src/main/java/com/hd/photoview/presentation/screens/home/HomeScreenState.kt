package com.hd.photoview.presentation.screens.home

import com.hd.photoview.data.remote.dto.PhotoItem


data class HomeScreenState(
    val listPhoto: List<PhotoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error : String = ""
)