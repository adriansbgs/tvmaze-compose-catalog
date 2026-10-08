package com.example.tvpulse.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tvpulse.data.local.FavoriteShowDao
import com.example.tvpulse.data.local.FavoriteShowEntity

@Database(entities = [FavoriteShowEntity::class], version = 1, exportSchema = false)
abstract class TvPulseDatabase : RoomDatabase() {
    abstract fun favoriteShowDao(): FavoriteShowDao
}
