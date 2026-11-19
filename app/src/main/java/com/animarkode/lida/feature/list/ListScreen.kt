package com.animarkode.lida.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animarkode.lida.R
import com.animarkode.lida.ui.components.WatchItemCard

@Composable
fun ListScreen(
    onAdd: () -> Unit,
    onOpen: (String) -> Unit,
    viewModel: ListViewModel = viewModel(factory = ListViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ListContent(state = state, onAdd = onAdd, onOpen = onOpen, onIncrement = viewModel::onIncrementEpisode)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListContent(
    state: ListUiState,
    onAdd: () -> Unit,
    onOpen: (String) -> Unit,
    onIncrement: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val addLabel = stringResource(R.string.add)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.list_title)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd, modifier = Modifier.semantics { contentDescription = addLabel }) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        },
    ) { padding ->
        when {
            state.isLoading -> Unit
            state.items.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    stringResource(R.string.empty_list),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 88.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.items, key = { it.id }) { item ->
                    WatchItemCard(
                        item = item,
                        onClick = { onOpen(item.id) },
                        onIncrement = { onIncrement(item.id) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}
