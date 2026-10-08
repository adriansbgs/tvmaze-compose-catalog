package com.example.tvpulse.domain.model

data class Show(
    val id: Int,
    val name: String,
    val imageMedium: String?,
    val imageOriginal: String?,
    val rating: Double?,
    val genres: List<String>,
    val runtime: Int?,
    val status: String?,
    val summary: String?, // Raw HTML from TVmaze, rendered with AnnotatedString.fromHtml in the UI
)
