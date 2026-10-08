package com.example.tvpulse.util

import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.ShowRepository

class FakeShowRepository : ShowRepository {
    var topResult: DataResult<List<Show>> = DataResult.Success(emptyList())
    var searchResult: DataResult<List<Show>> = DataResult.Success(emptyList())
    var showResult: DataResult<Show> = DataResult.Success(testShow(1))

    var topCallCount = 0
        private set
    val searchQueries = mutableListOf<String>()
    var showCallCount = 0
        private set

    override suspend fun getTopShows(): DataResult<List<Show>> {
        topCallCount++
        return topResult
    }

    override suspend fun searchShows(query: String): DataResult<List<Show>> {
        searchQueries += query
        return searchResult
    }

    override suspend fun getShow(id: Int): DataResult<Show> {
        showCallCount++
        return showResult
    }
}
