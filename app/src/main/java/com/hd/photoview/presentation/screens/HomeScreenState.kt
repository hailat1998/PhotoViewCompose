package com.hd.photoview.presentation.screens

import com.hd.photoview.data.remote.dto.PhotoItem


data class HomeScreenState(
    val listPhoto: List<PhotoItem> = emptyList<PhotoItem>(),
    val isLoading: Boolean = false,
    val error : String = ""
)