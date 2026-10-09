package com.example.tvpulse.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    // Room re-emits on every insert/delete, so changes made on Detail show up here immediately.
    val uiState: StateFlow<UiState<List<Show>>> =
        favoriteRepository.observeFavorites()
            .map { if (it.isEmpty()) UiState.Empty else UiState.Success(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    fun remove(id: Int) {
        viewModelScope.launch { favoriteRepository.remove(id) }
    }
}
