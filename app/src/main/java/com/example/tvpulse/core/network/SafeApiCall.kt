package com.example.tvpulse.core.network

import com.example.tvpulse.core.common.DataResult
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(block: suspend () -> T): DataResult<T> =
    try {
        DataResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        DataResult.Failure(e.toAppError())
    }
