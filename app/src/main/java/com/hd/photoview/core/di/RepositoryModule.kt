package com.hd.photoview.core.di

import com.hd.photoview.data.repository.PhotoRepositoryImpl
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotoImplementation(
        repositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository

}