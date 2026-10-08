package com.example.tvpulse.data.mapper

import com.example.tvpulse.data.local.FavoriteShowEntity
import com.example.tvpulse.domain.model.Show

private const val GENRE_SEPARATOR = "|"

fun Show.toEntity(savedAt: Long = System.currentTimeMillis()): FavoriteShowEntity =
    FavoriteShowEntity(
        id = id,
        name = name,
        imageMedium = imageMedium,
        imageOriginal = imageOriginal,
        rating = rating,
        genres = genres.joinToString(GENRE_SEPARATOR),
        runtime = runtime,
        status = status,
        summary = summary,
        savedAt = savedAt,
    )

fun FavoriteShowEntity.toDomain(): Show = Show(
    id = id,
    name = name,
    imageMedium = imageMedium,
    imageOriginal = imageOriginal,
    rating = rating,
    genres = if (genres.isEmpty()) emptyList() else genres.split(GENRE_SEPARATOR),
    runtime = runtime,
    status = status,
    summary = summary,
)
