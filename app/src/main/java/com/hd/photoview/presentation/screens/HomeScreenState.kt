package com.hd.photoview.presentation.screens

import com.hd1998.photofetch.api.PhotoItem

data class HomeScreenState(
    val listPhoto: List<PhotoItem> = emptyList<PhotoItem>() ,
    val isLoading: Boolean = false ,
    val error : String = ""
)