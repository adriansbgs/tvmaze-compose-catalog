package com.example.tvpulse.data.mapper

import com.example.tvpulse.data.remote.dto.ShowDto
import com.example.tvpulse.domain.model.Show

fun ShowDto.toDomain(): Show = Show(
    id = id,
    name = name,
    imageMedium = image?.medium,
    imageOriginal = image?.original,
    rating = rating?.average,
    genres = genres,
    runtime = runtime,
    status = status,
    summary = summary,
)
