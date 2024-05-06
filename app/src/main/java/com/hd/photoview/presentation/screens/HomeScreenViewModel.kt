package com.hd.photoview.presentation.screens

import androidx.lifecycle.ViewModel
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor( private val photoRepository: PhotoRepository) : ViewModel() {

}