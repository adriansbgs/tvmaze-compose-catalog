package com.example.tvpulse.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tvpulse.R
import com.example.tvpulse.core.designsystem.theme.TVPulseTheme
import com.example.tvpulse.domain.model.Show

@Composable
fun ShowCard(show: Show, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
    ) {
        ShowPoster(
            url = show.imageMedium,
            contentDescription = show.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
        )
        Text(
            text = show.name,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        show.rating?.let {
            Text(
                text = stringResource(R.string.rating_format, it),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
private fun ShowCardPreview() {
    TVPulseTheme {
        ShowCard(
            show = Show(1, "Breaking Bad", null, null, 9.2, listOf("Drama"), 60, "Ended", null),
            onClick = {},
            modifier = Modifier.width(160.dp),
        )
    }
}
