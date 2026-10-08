package com.example.tvpulse.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tvpulse.R
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.core.designsystem.component.EmptyState
import com.example.tvpulse.core.designsystem.component.ShimmerBox
import com.example.tvpulse.core.designsystem.component.ShowCard
import com.example.tvpulse.core.designsystem.component.UiStateContent
import com.example.tvpulse.core.designsystem.theme.TVPulseTheme
import com.example.tvpulse.domain.model.Show

@Composable
fun HomeRoute(
    onShowClick: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        query = query,
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onRefresh = viewModel::refresh,
        onShowClick = onShowClick,
    )
}

@Composable
fun HomeScreen(
    query: String,
    uiState: UiState<List<Show>>,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        SearchField(query = query, onQueryChange = onQueryChange)
        UiStateContent(
            state = uiState,
            onRefresh = onRefresh,
            loading = { ShowGridShimmer() },
            empty = {
                EmptyState(
                    title = stringResource(R.string.search_empty_title),
                    subtitle = stringResource(R.string.search_empty_subtitle),
                    icon = Icons.Outlined.SearchOff,
                )
            },
        ) { shows ->
            ShowGrid(
                title = stringResource(
                    if (query.isBlank()) R.string.home_title_popular else R.string.home_title_search,
                    shows.size,
                ),
                shows = shows,
                onShowClick = onShowClick,
            )
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = stringResource(R.string.search_clear)
                    )
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun ShowGrid(title: String, shows: List<Show>, onShowClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(title, style = MaterialTheme.typography.titleSmall)
        }
        items(shows, key = { it.id }) { show ->
            ShowCard(show = show, onClick = { onShowClick(show.id) })
        }
    }
}

@Composable
private fun ShowGridShimmer() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(6) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f)
                )
                ShimmerBox(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(14.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    TVPulseTheme {
        Surface { HomeScreen("", UiState.Loading, {}, {}, {}) }
    }
}
