package com.animarkode.lida.feature.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.animarkode.lida.LidaApp
import com.animarkode.lida.data.WatchRepository
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: WatchRepository,
) : ViewModel() {
    private val itemId: String? = savedStateHandle.get<String>("id")
    private var original: WatchItem? = null

    private val _uiState = MutableStateFlow(EditUiState(isNew = itemId == null))
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    init {
        if (itemId != null) {
            viewModelScope.launch {
                val item = repository.getItem(itemId) ?: return@launch
                original = item
                _uiState.update {
                    it.copy(
                        title = item.title,
                        kind = item.kind,
                        season = item.season?.toString().orEmpty(),
                        episode = item.episode?.toString().orEmpty(),
                        timestamp = item.timestamp.orEmpty(),
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, titleError = false) }

    fun onKindChange(value: Kind) = _uiState.update { it.copy(kind = value) }

    fun onSeasonChange(value: String) {
        if (value.all(Char::isDigit)) _uiState.update { it.copy(season = value) }
    }

    fun onEpisodeChange(value: String) {
        if (value.all(Char::isDigit)) _uiState.update { it.copy(episode = value) }
    }

    fun onTimestampChange(value: String) = _uiState.update { it.copy(timestamp = value, timestampError = false) }

    fun save() {
        val state = _uiState.value
        val title = state.title.trim()
        val timestamp = state.timestamp.trim()
        val titleError = title.isEmpty()
        val timestampError = !isValidTimestamp(timestamp)
        if (titleError || timestampError) {
            _uiState.update { it.copy(titleError = titleError, timestampError = timestampError) }
            return
        }
        val isSeries = state.kind == Kind.SERIES
        val base = original ?: WatchItem(title = title, kind = state.kind, updatedAt = 0)
        val item = base.copy(
            title = title,
            kind = state.kind,
            season = if (isSeries) state.season.toIntOrNull() else null,
            episode = if (isSeries) state.episode.toIntOrNull() else null,
            timestamp = timestamp.ifEmpty { null },
        )
        viewModelScope.launch {
            repository.save(item)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun delete() {
        val id = itemId ?: return
        viewModelScope.launch {
            repository.delete(id)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                EditViewModel(
                    createSavedStateHandle(),
                    (this[APPLICATION_KEY] as LidaApp).container.watchRepository,
                )
            }
        }
    }
}
