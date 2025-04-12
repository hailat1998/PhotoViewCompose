package com.hd.photoview.presentation.screens.search

sealed class SearchEvent {
    data class SearchPhoto(val query: String): SearchEvent()
}