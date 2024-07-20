package com.hd.photoview.core.di

import com.hd.photoview.data.remote.dto.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePhotos(): UnsplashApi {
       return Retrofit.Builder()
           .baseUrl("https://api.unsplash.com/")
           .addConverterFactory(MoshiConverterFactory.create())
           .client(
               OkHttpClient.Builder()
                   .connectTimeout(30, TimeUnit.SECONDS)
                   .readTimeout(30, TimeUnit.SECONDS)
                   .writeTimeout(30, TimeUnit.SECONDS)
                   .addInterceptor(HttpLoggingInterceptor().apply {
                       level = HttpLoggingInterceptor.Level.BODY
                   }).build()
           )
           .build()
           .create(UnsplashApi::class.java)
   }

    @Provides
    @Singleton
    @IoDispatcher
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

}


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher
