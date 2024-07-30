package com.hd.photoview.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.CustomNavType
import com.hd.photoview.presentation.screens.PhotoDetail
import com.hd.photoview.presentation.screens.Routes
import com.hd.photoview.presentation.screens.WebView
import com.hd.photoview.presentation.screens.home.HomeScreen
import com.hd.photoview.presentation.screens.home.HomeScreenEvents
import com.hd.photoview.presentation.screens.home.HomeScreenViewModel
import kotlin.reflect.typeOf

@Composable
fun MainHost(navHostController: NavHostController){
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    NavHost(navController = navHostController, startDestination = Routes.Home ) {


        composable<Routes.Home> {
            val photos = viewModel.photoList.collectAsLazyPagingItems()
             HomeScreen(photos = photos, onEvent =  { viewModel.onEvents(it) }, toDetail = { it ->
                 navHostController.navigate(Routes.DetailScreen(it))
             })
        }

        composable<Routes.WebScreen> { backStackEntry ->
               val arg = backStackEntry.toRoute<Routes.WebScreen>()
            WebView(url = arg.url)
        }

        composable<Routes.DetailScreen>( typeMap = mapOf(typeOf<Photo>() to
                CustomNavType(Photo::class.java, Photo.serializer()))) { backStackEntry ->
            val parameters = backStackEntry.toRoute<Routes.DetailScreen>()
              PhotoDetail(photo = parameters.photo) {
                  viewModel.onEvents(it)
              }
        }
    }
}