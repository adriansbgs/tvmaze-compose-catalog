package com.example.tvpulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_shows")
data class FavoriteShowEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageMedium: String?,
    val imageOriginal: String?,
    val rating: Double?,
    val genres: String,
    val runtime: Int?,
    val status: String?,
    val summary: String?,
    val savedAt: Long,
)
