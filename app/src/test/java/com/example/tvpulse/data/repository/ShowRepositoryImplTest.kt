package com.example.tvpulse.data.repository

import com.example.tvpulse.core.common.AppError
import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.data.remote.dto.SearchResultDto
import com.example.tvpulse.util.FakeTvMazeApi
import com.example.tvpulse.util.testShowDto
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ShowRepositoryImplTest {
    private val api = FakeTvMazeApi()
    private val repository = ShowRepositoryImpl(api)

    @Test
    fun `getTopShows keeps only the first 30 of the response`() = runTest {
        api.shows = (1..240).map { testShowDto(it) }

        val result = repository.getTopShows() as DataResult.Success

        assertEquals(30, result.data.size)
        assertEquals((1..30).toList(), result.data.map { it.id })
    }

    @Test
    fun `searchShows unwraps score wrapper and passes the query`() = runTest {
        api.searchResults = listOf(
            SearchResultDto(score = 0.9, show = testShowDto(1)),
            SearchResultDto(score = 0.5, show = testShowDto(2)),
        )

        val result = repository.searchShows("batman") as DataResult.Success

        assertEquals("batman", api.lastQuery)
        assertEquals(listOf(1, 2), result.data.map { it.id })
    }

    @Test
    fun `getShow returns the requested show`() = runTest {
        api.shows = listOf(testShowDto(169))

        val result = repository.getShow(169) as DataResult.Success

        assertEquals(169, result.data.id)
    }

    @Test
    fun `IOException maps to NoConnection`() = runTest {
        api.error = IOException("offline")

        assertEquals(DataResult.Failure(AppError.NoConnection), repository.getTopShows())
    }

    @Test
    fun `SocketTimeoutException maps to Timeout`() = runTest {
        api.error = SocketTimeoutException()

        assertEquals(DataResult.Failure(AppError.Timeout), repository.getTopShows())
    }

    @Test
    fun `HTTP 404 maps to NotFound and 500 to Server`() = runTest {
        api.error = HttpException(Response.error<Any>(404, "".toResponseBody()))
        assertEquals(DataResult.Failure(AppError.NotFound), repository.getShow(999))

        api.error = HttpException(Response.error<Any>(500, "".toResponseBody()))
        assertEquals(DataResult.Failure(AppError.Server(500)), repository.getShow(999))
    }
}
