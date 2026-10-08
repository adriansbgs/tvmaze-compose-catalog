package com.example.tvpulse.domain.repository

import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.domain.model.Show

interface ShowRepository {
    suspend fun getTopShows(): DataResult<List<Show>>
    suspend fun searchShows(query: String): DataResult<List<Show>>
    suspend fun getShow(id: Int): DataResult<Show>
}
