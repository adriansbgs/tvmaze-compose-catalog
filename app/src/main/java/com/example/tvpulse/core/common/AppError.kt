package com.example.tvpulse.core.common

sealed interface AppError {
    data object NoConnection : AppError
    data object Timeout : AppError
    data object NotFound : AppError
    data class Server(val code: Int) : AppError
    data class Unknown(val cause: Throwable?) : AppError
}