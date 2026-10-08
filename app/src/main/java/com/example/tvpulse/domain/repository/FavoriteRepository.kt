package com.example.tvpulse.domain.repository

import com.example.tvpulse.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavorites(): Flow<List<Show>>
    fun observeIsFavorite(id: Int): Flow<Boolean>
    suspend fun toggle(show: Show)
    suspend fun remove(id: Int)
}
