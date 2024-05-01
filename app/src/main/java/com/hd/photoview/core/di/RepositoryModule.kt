package com.hd.photoview.core.di

import com.hd.photoview.data.repository.PhotoRepositoryImpl
import com.hd.photoview.domain.repository.PhotoRepository
import dagger.Binds
import javax.inject.Singleton

abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPhotoImplementation(
        repositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository

}