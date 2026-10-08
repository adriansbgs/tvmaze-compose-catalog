package com.example.tvpulse.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tvpulse.R
import com.example.tvpulse.core.common.UiState

/**
 * Shared Loading / Success / Error / Empty switch with pull-to-refresh.
 * Error shows [ErrorDialog]; after "Tutup" a pull-to-refresh hint stays so the screen is never blank.
 */
@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    onRefresh: () -> Unit,
    loading: @Composable () -> Unit,
    empty: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    success: @Composable (T) -> Unit,
) {
    // isRefreshing is always false: shimmer is the only loading indicator (PDF Step 4)
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        when (state) {
            UiState.Loading -> loading()
            is UiState.Success -> success(state.data)
            UiState.Empty -> ScrollableCenter { empty() }
            is UiState.Error -> {
                // Error branch leaves composition on every Loading, so each new failure re-opens the dialog.
                var dismissed by remember(state) { mutableStateOf(false) }
                if (!dismissed) {
                    ErrorDialog(state.error, onDismiss = { dismissed = true }, onRetry = onRefresh)
                }
                ScrollableCenter {
                    EmptyState(
                        title = stringResource(R.string.pull_to_refresh),
                        icon = Icons.Outlined.CloudOff
                    )
                }
            }
        }
    }
}

/** Pull-to-refresh needs scrollable content, this keeps short content centered yet scrollable. */
@Composable
private fun ScrollableCenter(content: @Composable () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
