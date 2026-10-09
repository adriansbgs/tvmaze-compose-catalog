package com.example.tvpulse.ui

import androidx.lifecycle.SavedStateHandle
import com.example.tvpulse.core.common.AppError
import com.example.tvpulse.core.common.DataResult
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.ui.detail.DetailContent
import com.example.tvpulse.ui.detail.DetailViewModel
import com.example.tvpulse.util.FakeFavoriteRepository
import com.example.tvpulse.util.FakeShowRepository
import com.example.tvpulse.util.MainDispatcherRule
import com.example.tvpulse.util.testShow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val showRepository = FakeShowRepository()
    private val favoriteRepository = FakeFavoriteRepository()
    private val show = testShow(169, "Breaking Bad")

    // "id" is the property name of DetailRouteKey; the same key a tap or a deeplink puts in the back stack entry.
    private fun createViewModel() = DetailViewModel(
        showRepository = showRepository,
        favoriteRepository = favoriteRepository,
        savedStateHandle = SavedStateHandle(mapOf("id" to 169)),
    )

    // uiState uses stateIn(WhileSubscribed), so it only updates while someone collects it — like the screen does.
    private fun kotlinx.coroutines.test.TestScope.collect(viewModel: DetailViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
    }

    @Test
    fun `loads the show from the route id`() = runTest {
        showRepository.showResult = DataResult.Success(show)
        val viewModel = createViewModel()
        collect(viewModel)

        assertEquals(UiState.Success(DetailContent(show, isFavorite = false)), viewModel.uiState.value)
        assertEquals(1, showRepository.showCallCount)
    }

    @Test
    fun `toggleFavorite updates isFavorite through the repository flow`() = runTest {
        showRepository.showResult = DataResult.Success(show)
        val viewModel = createViewModel()
        collect(viewModel)

        viewModel.toggleFavorite()
        assertEquals(UiState.Success(DetailContent(show, isFavorite = true)), viewModel.uiState.value)

        viewModel.toggleFavorite()
        assertEquals(UiState.Success(DetailContent(show, isFavorite = false)), viewModel.uiState.value)
    }

    @Test
    fun `toggleFavorite does nothing while the show is not loaded`() = runTest {
        showRepository.showResult = DataResult.Failure(AppError.NoConnection)
        val viewModel = createViewModel()
        collect(viewModel)

        viewModel.toggleFavorite()

        assertEquals(UiState.Error(AppError.NoConnection), viewModel.uiState.value)
    }

    @Test
    fun `failure becomes Error and refresh retries`() = runTest {
        showRepository.showResult = DataResult.Failure(AppError.NotFound)
        val viewModel = createViewModel()
        collect(viewModel)
        assertEquals(UiState.Error(AppError.NotFound), viewModel.uiState.value)

        showRepository.showResult = DataResult.Success(show)
        viewModel.refresh()

        assertEquals(2, showRepository.showCallCount)
        assertEquals(UiState.Success(DetailContent(show, isFavorite = false)), viewModel.uiState.value)
    }
}
