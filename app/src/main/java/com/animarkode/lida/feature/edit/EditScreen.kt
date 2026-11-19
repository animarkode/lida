package com.animarkode.lida.feature.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animarkode.lida.R
import com.animarkode.lida.data.model.Kind

@Composable
fun EditScreen(
    onDone: () -> Unit,
    viewModel: EditViewModel = viewModel(factory = EditViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved) { if (state.isSaved) onDone() }
    EditContent(
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onKindChange = viewModel::onKindChange,
        onSeasonChange = viewModel::onSeasonChange,
        onEpisodeChange = viewModel::onEpisodeChange,
        onTimestampChange = viewModel::onTimestampChange,
        onSave = viewModel::save,
        onDelete = viewModel::delete,
        onBack = onDone,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContent(
    state: EditUiState,
    onTitleChange: (String) -> Unit,
    onKindChange: (Kind) -> Unit,
    onSeasonChange: (String) -> Unit,
    onEpisodeChange: (String) -> Unit,
    onTimestampChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (state.isNew) R.string.edit_title_new else R.string.edit_title_existing)) },
                navigationIcon = { TextButton(onClick = onBack) { Text("←", style = MaterialTheme.typography.titleLarge) } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.field_title)) },
                isError = state.titleError,
                supportingText = if (state.titleError) ({ Text(stringResource(R.string.error_title)) }) else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            val kinds = listOf(Kind.MOVIE to R.string.kind_movie, Kind.SERIES to R.string.kind_series)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                kinds.forEachIndexed { index, (kind, label) ->
                    SegmentedButton(
                        selected = state.kind == kind,
                        onClick = { onKindChange(kind) },
                        shape = SegmentedButtonDefaults.itemShape(index, kinds.size),
                    ) { Text(stringResource(label)) }
                }
            }
            if (state.kind == Kind.SERIES) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberField(state.season, onSeasonChange, R.string.field_season, Modifier.weight(1f))
                    NumberField(state.episode, onEpisodeChange, R.string.field_episode, Modifier.weight(1f))
                }
            }
            OutlinedTextField(
                value = state.timestamp,
                onValueChange = onTimestampChange,
                label = { Text(stringResource(R.string.field_timestamp)) },
                isError = state.timestampError,
                supportingText = if (state.timestampError) ({ Text(stringResource(R.string.error_timestamp)) }) else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.save)) }
            if (!state.isNew) {
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun NumberField(value: String, onChange: (String) -> Unit, label: Int, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}
