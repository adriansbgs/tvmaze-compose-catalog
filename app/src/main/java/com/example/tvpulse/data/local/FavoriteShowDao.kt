package com.example.tvpulse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteShowDao {

    @Query("SELECT * FROM favorite_shows ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<FavoriteShowEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_shows WHERE id = :id)")
    fun observeIsFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(show: FavoriteShowEntity)

    @Query("DELETE FROM favorite_shows WHERE id = :id")
    suspend fun delete(id: Int)
}
