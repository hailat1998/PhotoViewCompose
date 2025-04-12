package com.hd.photoview.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.CustomNavType
import com.hd.photoview.presentation.screens.Routes
import com.hd.photoview.presentation.screens.WebView
import com.hd.photoview.presentation.screens.details.DetailsViewModel
import com.hd.photoview.presentation.screens.details.PhotoDetail
import com.hd.photoview.presentation.screens.home.HomeScreen
import com.hd.photoview.presentation.screens.home.HomeScreenViewModel
import com.hd.photoview.presentation.screens.search.SearchEvent
import com.hd.photoview.presentation.screens.search.SearchScreen
import com.hd.photoview.presentation.screens.search.SearchViewModel
import com.hd.photoview.presentation.utils.toDecoded
import kotlin.reflect.typeOf


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainHost(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = Routes.Home ) {

        composable<Routes.Home> {

            val viewModel = hiltViewModel<HomeScreenViewModel>()

            val photos = remember { viewModel.photoList }.collectAsLazyPagingItems()

            HomeScreen(
                photos = photos,
                onEvent = { viewModel.onEvents(it) },
                toDetail = { photo ->
                    val decodedPhoto = photo.toDecoded()
                    navHostController.navigate(Routes.DetailScreen(decodedPhoto))
                    {
                        restoreState = true
                    }
                },
                toSearch = {navHostController.navigate(Routes.Search) {
                    restoreState = true
                }
              }
            )
         }

        composable<Routes.Search> {
            val viewModel = hiltViewModel<SearchViewModel>()
            viewModel.onEvents(SearchEvent.SearchPhoto(" "))

            val photos = remember {
                viewModel.searchPhotoList
            }.collectAsLazyPagingItems()

            SearchScreen(photos, onEvent = { viewModel::onEvents }, toDetail ={
                    photo ->
                val decodedPhoto = photo.toDecoded()
                navHostController.navigate(Routes.DetailScreen(decodedPhoto)) {
                    restoreState = true
                }
            }
            )
        }

        composable<Routes.WebScreen> { backStackEntry ->
               val arg = backStackEntry.toRoute<Routes.WebScreen>()
            WebView(id = arg.id, desc = arg.alt_desc)
        }

        composable<Routes.DetailScreen>( typeMap = mapOf(typeOf<Photo>() to
                CustomNavType(Photo::class.java, Photo.serializer()))) { backStackEntry ->

            val viewModel = hiltViewModel<DetailsViewModel>()

                val parameters = backStackEntry.toRoute<Routes.DetailScreen>()

                Log.i("FROM NAV", parameters.photo.description)

              PhotoDetail(photo = parameters.photo , onEvent =  {
                  viewModel.onEvent(it)
              }, toWeb = { id, altDesc ->
                  navHostController.navigate(Routes.WebScreen(id, altDesc))
                 }
              )
        }
    }
}



