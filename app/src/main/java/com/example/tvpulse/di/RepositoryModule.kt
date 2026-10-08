package com.example.tvpulse.di

import com.example.tvpulse.data.repository.FavoriteRepositoryImpl
import com.example.tvpulse.data.repository.ShowRepositoryImpl
import com.example.tvpulse.domain.repository.FavoriteRepository
import com.example.tvpulse.domain.repository.ShowRepository
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
    abstract fun bindShowRepository(impl: ShowRepositoryImpl): ShowRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}
