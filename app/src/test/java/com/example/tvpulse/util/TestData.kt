package com.example.tvpulse.util

import com.example.tvpulse.data.remote.dto.ShowDto
import com.example.tvpulse.domain.model.Show

fun testShow(id: Int, name: String = "Show $id") = Show(
    id = id,
    name = name,
    imageMedium = "https://static.tvmaze.com/medium/$id.jpg",
    imageOriginal = "https://static.tvmaze.com/original/$id.jpg",
    rating = 8.5,
    genres = listOf("Drama", "Thriller"),
    runtime = 60,
    status = "Ended",
    summary = "<p><b>$name</b> summary</p>",
)

fun testShowDto(id: Int) = ShowDto(id = id, name = "Show $id")
