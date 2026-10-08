package com.example.tvpulse.di

import android.content.Context
import androidx.room.Room
import com.example.tvpulse.core.database.TvPulseDatabase
import com.example.tvpulse.data.local.FavoriteShowDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TvPulseDatabase =
        Room.databaseBuilder(context, TvPulseDatabase::class.java, "tvpulse.db").build()

    @Provides
    fun provideFavoriteShowDao(db: TvPulseDatabase): FavoriteShowDao = db.favoriteShowDao()
}
