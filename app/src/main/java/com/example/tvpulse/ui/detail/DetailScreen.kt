package com.example.tvpulse.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tvpulse.R
import com.example.tvpulse.core.common.UiState
import com.example.tvpulse.core.designsystem.component.ShimmerBox
import com.example.tvpulse.core.designsystem.component.ShowPoster
import com.example.tvpulse.core.designsystem.component.UiStateContent
import com.example.tvpulse.core.designsystem.theme.TVPulseTheme
import com.example.tvpulse.domain.model.Show

@Composable
fun DetailRoute(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRefresh = viewModel::refresh,
        onToggleFavorite = viewModel::toggleFavorite,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: UiState<DetailContent>,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (uiState as? UiState.Success)?.data?.show?.name.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.action_back))
                    }
                },
            )
        },
    ) { padding ->
        UiStateContent(
            state = uiState,
            onRefresh = onRefresh,
            loading = { DetailShimmer() },
            empty = {}, // a single show is never Empty
            modifier = Modifier.padding(padding),
        ) { content ->
            DetailBody(content = content, onToggleFavorite = onToggleFavorite)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailBody(content: DetailContent, onToggleFavorite: () -> Unit) {
    val show = content.show
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ShowPoster(
            url = show.imageOriginal ?: show.imageMedium,
            contentDescription = show.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            show.genres.forEach { InfoChip(it) }
            show.runtime?.let { InfoChip(stringResource(R.string.detail_runtime, it)) }
            show.status?.let { InfoChip(it) }
        }
        if (content.isFavorite) {
            OutlinedButton(onClick = onToggleFavorite, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.detail_remove_favorite))
            }
        } else {
            Button(onClick = onToggleFavorite, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.detail_add_favorite))
            }
        }
        show.summary?.let { html ->
            Text(stringResource(R.string.detail_synopsis), style = MaterialTheme.typography.titleMedium)
            val summary = remember(html) { AnnotatedString.fromHtml(html) }
            Text(summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun InfoChip(label: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun DetailShimmer() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ShimmerBox(Modifier.fillMaxWidth().height(360.dp))
        ShimmerBox(Modifier.width(220.dp).height(24.dp))
        ShimmerBox(Modifier.fillMaxWidth().height(40.dp))
        repeat(4) { ShimmerBox(Modifier.fillMaxWidth().height(14.dp)) }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    TVPulseTheme {
        DetailScreen(
            uiState = UiState.Success(
                DetailContent(
                    Show(1, "Stranger Things", null, null, 8.7, listOf("Drama", "Horror", "Sci-Fi"), 60, "Running",
                        "<p><b>Stranger Things</b> is a mystery.</p>"),
                    isFavorite = false,
                )
            ),
            onBackClick = {}, onRefresh = {}, onToggleFavorite = {},
        )
    }
}
