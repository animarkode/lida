package com.animarkode.lida.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.animarkode.lida.LidaApp
import com.animarkode.lida.data.WatchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(private val repository: WatchRepository) : ViewModel() {
    val uiState: StateFlow<ListUiState> = repository.observeItems()
        .map { ListUiState(items = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ListUiState())

    fun onIncrementEpisode(id: String) {
        viewModelScope.launch { repository.incrementEpisode(id) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListViewModel((this[APPLICATION_KEY] as LidaApp).container.watchRepository)
            }
        }
    }
}
