package com.example.tvpulse.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShowDto(
    val id: Int,
    val name: String,
    val genres: List<String> = emptyList(),
    val status: String? = null,
    val runtime: Int? = null,
    val rating: RatingDto? = null,
    val image: ImageDto? = null,
    val summary: String? = null,
)

@Serializable
data class RatingDto(val average: Double? = null)

@Serializable
data class ImageDto(val medium: String? = null, val original: String? = null)

@Serializable
data class SearchResultDto(val score: Double = 0.0, val show: ShowDto)
