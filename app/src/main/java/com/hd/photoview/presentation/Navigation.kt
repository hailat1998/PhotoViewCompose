package com.hd.photoview.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.CustomNavType
import com.hd.photoview.presentation.screens.Routes
import com.hd.photoview.presentation.screens.WebView
import com.hd.photoview.presentation.screens.details.DetailsViewModel
import com.hd.photoview.presentation.screens.details.PhotoDetail
import com.hd.photoview.presentation.screens.home.HomeScreen
import com.hd.photoview.presentation.screens.home.HomeScreenViewModel
import com.hd.photoview.presentation.screens.search.SearchScreen
import com.hd.photoview.presentation.screens.search.SearchViewModel
import com.hd.photoview.presentation.screens.user.UserScreen
import com.hd.photoview.presentation.screens.user.UserViewModel
import com.hd.photoview.presentation.utils.toDecoded
import kotlin.reflect.typeOf

const val ANIMATION_DURATION = 700

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainHost(navHostController: NavHostController) {

    SharedTransitionLayout {
        NavHost(navController = navHostController, startDestination = Routes.Home) {

            composable<Routes.Home> {

                val viewModel = hiltViewModel<HomeScreenViewModel>()

                val photos = remember { viewModel.photoList }

                HomeScreen(
                    photosData = photos,
                    toDetail = { photo ->
                        val decodedPhoto = photo.toDecoded()
                        navHostController.navigate(Routes.DetailScreen(decodedPhoto))
                        {
                            restoreState = true
                        }
                    },
                    toSearch = {
                        navHostController.navigate(Routes.Search) {
                            restoreState = true
                        }
                    },
                    animatedVisibilityScope = this
                )
            }

            composable<Routes.UserScreen>(
                typeMap =
                mapOf(typeOf<Photo>() to CustomNavType(Photo::class.java, Photo.serializer())),
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(ANIMATION_DURATION)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(ANIMATION_DURATION)
                    )
                }
            ) { backStackEntry ->

                val arg = backStackEntry.toRoute<Routes.UserScreen>()
                val viewModel: UserViewModel = hiltViewModel()
                val photosData = remember { viewModel.userPhotos(arg.photo.username) }

                UserScreen(photosData,arg.photo, this){ photo ->
                    val decodedPhoto = photo.toDecoded()
                    navHostController.navigate(Routes.DetailScreen(decodedPhoto))
                    {
                        restoreState = true
                    }
                }
            }

            composable<Routes.Search>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(ANIMATION_DURATION)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(ANIMATION_DURATION)
                    )
                }
            ) {
                val viewModel = hiltViewModel<SearchViewModel>()
                val queryState = remember { mutableStateOf("") }


                val searchTrigger = remember { mutableIntStateOf(0) }

                val photos = remember(searchTrigger.value) {

                    viewModel.searchPhotoList(queryState.value)
                }

                SearchScreen(
                    photosData = photos,
                    toDetail = { photo ->
                        val decodedPhoto = photo.toDecoded()
                        navHostController.navigate(Routes.DetailScreen(decodedPhoto)) {
                            restoreState = true
                        }
                    },
                    query = queryState,
                    onSearch = {
                        searchTrigger.value++
                    },
                    animatedVisibilityScope = this,
                    onBack = {
                        navHostController.popBackStack()
                    }
                )
            }

            composable<Routes.WebScreen>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(ANIMATION_DURATION)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(ANIMATION_DURATION)
                    )
                }
            ) { backStackEntry ->

                val arg = backStackEntry.toRoute<Routes.WebScreen>()
                WebView(id = arg.id, desc = arg.alt_desc)
            }

            composable<Routes.DetailScreen>(typeMap =
            mapOf(typeOf<Photo>() to CustomNavType(Photo::class.java, Photo.serializer())),
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(ANIMATION_DURATION)
                    )
                },
                popExitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(ANIMATION_DURATION)
                    )
                }
            ) { backStackEntry ->

                val viewModel = hiltViewModel<DetailsViewModel>()

                val parameters = backStackEntry.toRoute<Routes.DetailScreen>()

                PhotoDetail(photo = parameters.photo, onEvent = {
                    viewModel.onEvent(it)
                },
                    toWeb = { id, altDesc ->
                    navHostController.navigate(Routes.WebScreen(id, altDesc))
                },
                    animatedVisibilityScope = this,
                    toUser = { photo ->
                        val decodedPhoto = photo.toDecoded()
                        navHostController.navigate(Routes.UserScreen(decodedPhoto))
                        {
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
