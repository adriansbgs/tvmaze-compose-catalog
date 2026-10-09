package com.example.tvpulse.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.core.common.toUiState
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.FavoriteRepository
import com.example.tvpulse.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailContent(val show: Show, val isFavorite: Boolean)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Read the arg by its key ("id" = DetailRouteKey.id) instead of toRoute(), which needs an Android Bundle
    // and fails in JVM unit tests. Taps and movieapp://detail/{id} deeplinks both land here.
    private val showId: Int = checkNotNull(savedStateHandle["id"])

    private val showState = MutableStateFlow<UiState<Show>>(UiState.Loading)
    private var loadJob: Job? = null

    val uiState: StateFlow<UiState<DetailContent>> =
        combine(showState, favoriteRepository.observeIsFavorite(showId)) { state, isFavorite ->
            when (state) {
                is UiState.Success -> UiState.Success(DetailContent(state.data, isFavorite))
                is UiState.Error -> state
                UiState.Loading -> UiState.Loading
                UiState.Empty -> UiState.Empty
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    init {
        refresh()
    }

    fun refresh() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            showState.value = UiState.Loading
            showState.value = showRepository.getShow(showId).toUiState()
        }
    }

    fun toggleFavorite() {
        val show = (showState.value as? UiState.Success)?.data ?: return
        viewModelScope.launch { favoriteRepository.toggle(show) }
    }
}
