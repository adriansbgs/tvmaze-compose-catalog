package com.example.tvpulse.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class UiStateTest {

    @Test
    fun `non-empty list becomes Success`() {
        assertEquals(UiState.Success(listOf(1)), DataResult.Success(listOf(1)).toUiState())
    }

    @Test
    fun `empty list becomes Empty`() {
        assertEquals(UiState.Empty, DataResult.Success(emptyList<Int>()).toUiState())
    }

    @Test
    fun `single object becomes Success`() {
        assertEquals(UiState.Success("show"), DataResult.Success("show").toUiState())
    }

    @Test
    fun `failure becomes Error with same AppError`() {
        assertEquals(UiState.Error(AppError.NotFound), DataResult.Failure(AppError.NotFound).toUiState())
    }
}
