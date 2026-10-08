package com.example.tvpulse.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tvpulse.R
import com.example.tvpulse.core.common.AppError
import com.example.tvpulse.core.designsystem.theme.TVPulseTheme

@Composable
fun ErrorDialog(error: AppError, onDismiss: () -> Unit, onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(error.titleRes())) },
        text = { Text(error.message()) },
        confirmButton = {
            TextButton(onClick = onRetry) { Text(stringResource(R.string.action_retry)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_close)) }
        },
    )
}

@StringRes
private fun AppError.titleRes(): Int = when (this) {
    AppError.NoConnection, AppError.Timeout -> R.string.error_title_connection
    else -> R.string.error_title_generic
}

@Composable
private fun AppError.message(): String = when (this) {
    AppError.NoConnection -> stringResource(R.string.error_msg_no_connection)
    AppError.Timeout -> stringResource(R.string.error_msg_timeout)
    AppError.NotFound -> stringResource(R.string.error_msg_not_found)
    is AppError.Server -> stringResource(R.string.error_msg_server, code)
    is AppError.Unknown -> stringResource(R.string.error_msg_unknown)
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    TVPulseTheme { ErrorDialog(AppError.NoConnection, onDismiss = {}, onRetry = {}) }
}
