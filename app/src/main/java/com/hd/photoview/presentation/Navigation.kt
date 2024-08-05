package com.hd.photoview.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.CustomNavType
import com.hd.photoview.presentation.screens.PhotoDetail
import com.hd.photoview.presentation.screens.Routes
import com.hd.photoview.presentation.screens.WebView
import com.hd.photoview.presentation.screens.home.HomeScreen
import com.hd.photoview.presentation.screens.home.HomeScreenEvents
import com.hd.photoview.presentation.screens.home.HomeScreenViewModel
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.reflect.typeOf

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainHost(navHostController: NavHostController){
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    NavHost(navController = navHostController, startDestination = Routes.Home ) {


        composable<Routes.Home> {
            val type = remember { mutableIntStateOf(2)  }
            val query = remember{ mutableStateOf("") }
            val photos: LazyPagingItems<Photo> = if(type.intValue == 1){
                viewModel.onEvents(HomeScreenEvents.SearchPhoto(query.value))
                viewModel.searchPhotoList.collectAsLazyPagingItems()
            }else {
                viewModel.onEvents(HomeScreenEvents.LoadPhoto)
                viewModel.photoList.collectAsLazyPagingItems()}
             HomeScreen(photos = photos, type, query ,onEvent =  { viewModel.onEvents(it) }, toDetail = { it ->
                 val photo = it.toDecoded()
                 navHostController.navigate(Routes.DetailScreen(photo)){
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

                val parameters = backStackEntry.toRoute<Routes.DetailScreen>()

                Log.i("FROM NAV", parameters.photo.description)

              PhotoDetail(photo = parameters.photo , onEvent =  {
                  viewModel.onEvents(it)
              }, toWeb = { id, alt_desc ->
                  navHostController.navigate(Routes.WebScreen(id, alt_desc))
                 }
              )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Photo.toDecoded():Photo{
      return Photo(id = this.id.encodeString(), description = this.description.encodeString()
      ,full = this.full.encodeString(), small = this.small.encodeString(), regular = this.regular.encodeString())
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun String.encodeString():String{
    return URLEncoder.encode(this, UTF_8)
}
