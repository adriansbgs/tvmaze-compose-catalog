package com.example.tvpulse.core.network

import com.example.tvpulse.core.common.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppError(): AppError = when (this) {
    is SocketTimeoutException -> AppError.Timeout
    is IOException -> AppError.NoConnection
    is HttpException -> if (code() == 404) AppError.NotFound else AppError.Server(code())
    else -> AppError.Unknown(this)
}
