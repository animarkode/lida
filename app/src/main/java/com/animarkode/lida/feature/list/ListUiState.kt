package com.animarkode.lida.feature.list

import com.animarkode.lida.data.model.WatchItem

data class ListUiState(
    val items: List<WatchItem> = emptyList(),
    val isLoading: Boolean = true,
)
