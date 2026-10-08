package com.example.tvpulse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.core.common.toUiState
import com.example.tvpulse.domain.model.Show
import com.example.tvpulse.domain.repository.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val SEARCH_DEBOUNCE_MS = 400L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ShowRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<Show>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Show>>> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        @OptIn(FlowPreview::class)
        _query.debounce(SEARCH_DEBOUNCE_MS.milliseconds).onEach { load(it) }.launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    /** Pull-to-refresh and the dialog's "Coba Lagi". */
    fun refresh() {
        load(_query.value)
    }

    private fun load(query: String) {
        loadJob?.cancel() // a newer query/refresh wins, the old result never overwrites it
        loadJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result =
                if (query.isBlank()) repository.getTopShows() else repository.searchShows(query)
            _uiState.value = result.toUiState()
        }
    }
}
