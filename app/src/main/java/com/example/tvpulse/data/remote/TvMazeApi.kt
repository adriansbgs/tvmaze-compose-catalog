package com.example.tvpulse.data.remote

import com.example.tvpulse.data.remote.dto.SearchResultDto
import com.example.tvpulse.data.remote.dto.ShowDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApi {

    @GET("shows")
    suspend fun getShows(): List<ShowDto>

    @GET("search/shows")
    suspend fun searchShows(@Query("q") query: String): List<SearchResultDto>

    @GET("shows/{id}")
    suspend fun getShow(@Path("id") id: Int): ShowDto
}
