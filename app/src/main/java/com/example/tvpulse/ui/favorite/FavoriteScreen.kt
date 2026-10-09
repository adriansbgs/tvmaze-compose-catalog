package com.example.tvpulse.ui.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tvpulse.R
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.core.designsystem.component.EmptyState
import com.example.tvpulse.core.designsystem.component.ShowPoster
import com.example.tvpulse.core.designsystem.theme.TVPulseTheme
import com.example.tvpulse.domain.model.Show

@Composable
fun FavoriteRoute(
    onShowClick: (Int) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoriteScreen(uiState, onShowClick, viewModel::remove)
}

@Composable
fun FavoriteScreen(
    uiState: UiState<List<Show>>,
    onShowClick: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is UiState.Success -> LazyColumn(modifier.fillMaxSize()) {
            item {
                Text(
                    text = stringResource(R.string.favorite_title, uiState.data.size),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(16.dp),
                )
            }
            items(uiState.data, key = { it.id }) { show ->
                FavoriteRow(
                    show = show,
                    onClick = { onShowClick(show.id) },
                    onRemove = { onRemove(show.id) },
                )
            }
        }

        // A local Room read finishes in milliseconds, so Loading renders nothing instead of a shimmer;
        // Room has no network, so Error never happens here.
        UiState.Loading, is UiState.Error -> Unit
        UiState.Empty -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyState(
                title = stringResource(R.string.favorite_empty_title),
                subtitle = stringResource(R.string.favorite_empty_subtitle),
                icon = Icons.Outlined.FavoriteBorder
            )
        }
    }
}

@Composable
private fun FavoriteRow(show: Show, onClick: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShowPoster(
            url = show.imageMedium,
            contentDescription = show.name,
            modifier = Modifier
                .width(64.dp)
                .aspectRatio(2f / 3f)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = show.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = show.genres.joinToString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        TextButton(onClick = onRemove) {
            Text(
                text = stringResource(R.string.favorite_remove),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteScreenEmptyPreview() {
    TVPulseTheme { Surface { FavoriteScreen(UiState.Empty, {}, {}) } }
}
