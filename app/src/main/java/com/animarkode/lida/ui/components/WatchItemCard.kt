package com.animarkode.lida.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.animarkode.lida.R
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem

@Composable
fun WatchItemCard(
    item: WatchItem,
    onClick: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                progressText(item)?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (item.kind == Kind.SERIES) {
                FilledTonalButton(onClick = onIncrement) {
                    Text(stringResource(R.string.increment_episode))
                }
            }
        }
    }
}

@Composable
private fun progressText(item: WatchItem): String? {
    val episode = if (item.kind == Kind.SERIES && item.episode != null) {
        stringResource(R.string.season_episode, item.season ?: 1, item.episode)
    } else {
        null
    }
    return listOfNotNull(episode, item.timestamp).joinToString("  ·  ").ifEmpty { null }
}
