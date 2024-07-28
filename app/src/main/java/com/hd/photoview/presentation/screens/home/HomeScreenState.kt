package com.hd.photoview.presentation.screens.home

import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.domain.model.Photo


data class HomeScreenState(
    val listPhoto: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val error : String = ""
)