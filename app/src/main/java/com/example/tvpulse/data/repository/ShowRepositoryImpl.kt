package com.example.tvpulse.data.repository

import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.core.network.safeApiCall
import com.example.tvpulse.data.mapper.toDomain
import com.example.tvpulse.data.remote.TvMazeApi
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.ShowRepository
import javax.inject.Inject

private const val TOP_SHOWS_LIMIT = 30

class ShowRepositoryImpl @Inject constructor(
    private val api: TvMazeApi,
) : ShowRepository {
    override suspend fun getTopShows(): DataResult<List<Show>> =
        safeApiCall { api.getShows().take(TOP_SHOWS_LIMIT).map { it.toDomain() } }

    override suspend fun searchShows(query: String): DataResult<List<Show>> =
        safeApiCall { api.searchShows(query).map { it.show.toDomain() } }

    override suspend fun getShow(id: Int): DataResult<Show> =
        safeApiCall { api.getShow(id).toDomain() }
}
