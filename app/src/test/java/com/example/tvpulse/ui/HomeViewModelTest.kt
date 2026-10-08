package com.example.tvpulse.ui

import com.example.tvpulse.core.common.AppError
import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.ui.home.HomeViewModel
import com.example.tvpulse.util.FakeShowRepository
import com.example.tvpulse.util.MainDispatcherRule
import com.example.tvpulse.util.testShow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * runTest shares the virtual clock of the Dispatchers.Main test dispatcher,
 * so advanceTimeBy drives the 400 ms debounce inside viewModelScope without real waiting.
 */
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeShowRepository()
    private val shows = listOf(testShow(1), testShow(2))

    @Test
    fun `blank query loads top shows`() = runTest {
        repository.topResult = DataResult.Success(shows)

        val viewModel = HomeViewModel(repository)
        advanceTimeBy(500)

        assertEquals(UiState.Success(shows), viewModel.uiState.value)
        assertEquals(1, repository.topCallCount)
    }

    @Test
    fun `typing quickly only searches the last query`() = runTest {
        val viewModel = HomeViewModel(repository)
        advanceTimeBy(500)

        viewModel.onQueryChange("b")
        advanceTimeBy(100)
        viewModel.onQueryChange("ba")
        advanceTimeBy(100)
        viewModel.onQueryChange("bat")
        advanceTimeBy(500)

        assertEquals(listOf("bat"), repository.searchQueries)
    }

    @Test
    fun `nothing is searched before the debounce window passes`() = runTest {
        val viewModel = HomeViewModel(repository)
        advanceTimeBy(500)

        viewModel.onQueryChange("batman")
        advanceTimeBy(300)

        assertEquals(emptyList<String>(), repository.searchQueries)
    }

    @Test
    fun `empty search result becomes Empty`() = runTest {
        repository.searchResult = DataResult.Success(emptyList())
        val viewModel = HomeViewModel(repository)

        viewModel.onQueryChange("zzzz")
        advanceTimeBy(500)

        assertEquals(UiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `failure becomes Error`() = runTest {
        repository.topResult = DataResult.Failure(AppError.NoConnection)

        val viewModel = HomeViewModel(repository)
        advanceTimeBy(500)

        assertEquals(UiState.Error(AppError.NoConnection), viewModel.uiState.value)
    }

    @Test
    fun `refresh reloads the active query without waiting for debounce`() = runTest {
        val viewModel = HomeViewModel(repository)
        viewModel.onQueryChange("batman")
        advanceTimeBy(500)

        viewModel.refresh()

        assertEquals(listOf("batman", "batman"), repository.searchQueries)
    }

    @Test
    fun `clearing the query goes back to top shows`() = runTest {
        // The initial "" is replaced by "batman" inside the debounce window,
        // so the only top-shows call is the one made after clearing.
        val viewModel = HomeViewModel(repository)
        viewModel.onQueryChange("batman")
        advanceTimeBy(500)

        viewModel.onQueryChange("")
        advanceTimeBy(500)

        assertEquals(1, repository.topCallCount)
        assertEquals(listOf("batman"), repository.searchQueries)
    }
}
