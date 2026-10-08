package com.example.tvpulse.util

import com.example.tvpulse.data.remote.TvMazeApi
import com.example.tvpulse.data.remote.dto.SearchResultDto
import com.example.tvpulse.data.remote.dto.ShowDto

class FakeTvMazeApi : TvMazeApi {
    var shows: List<ShowDto> = emptyList()
    var searchResults: List<SearchResultDto> = emptyList()
    var error: Throwable? = null
    var lastQuery: String? = null
        private set

    override suspend fun getShows(): List<ShowDto> {
        error?.let { throw it }
        return shows
    }

    override suspend fun searchShows(query: String): List<SearchResultDto> {
        error?.let { throw it }
        lastQuery = query
        return searchResults
    }

    override suspend fun getShow(id: Int): ShowDto {
        error?.let { throw it }
        return shows.first { it.id == id }
    }
}
