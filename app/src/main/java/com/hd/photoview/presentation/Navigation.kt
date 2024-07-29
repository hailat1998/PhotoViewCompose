package com.hd.photoview.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.CustomNavType
import com.hd.photoview.presentation.screens.Routes
import kotlin.reflect.typeOf

@Composable
fun MainHost(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = Routes.Home ) {


        composable<Routes.Home> {

        }


        composable<Routes.WebScreen> { backStackEntry ->

        }

        composable<Routes.DetailScreen>( typeMap = mapOf(typeOf<Photo>() to
                CustomNavType(Photo::class.java, Photo.serializer()))) { backStackEntry ->
            val parameters = backStackEntry.toRoute<Routes.DetailScreen>()

        }
    }
}