package com.hd.photoview.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hd.photoview.presentation.screens.home.HomeScreen
import com.hd.photoview.presentation.screens.home.HomeScreenViewModel
import com.hd.photoview.presentation.theme.PhotoViewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            PhotoViewTheme {
                val state = viewModel.state.collectAsStateWithLifecycle()
            HomeScreen(state = state.value){
                viewModel.onEvents(it)
              }
            }
        }
    }
}

