package com.example.tvpulse.core.common

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val error: AppError) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}

fun <T> DataResult<T>.toUiState(): UiState<T> = when (this) {
    is DataResult.Failure -> UiState.Error(error)
    is DataResult.Success -> if (data is Collection<*> && data.isEmpty()) UiState.Empty else UiState.Success(data)
}
