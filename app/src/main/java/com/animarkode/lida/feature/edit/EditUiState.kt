package com.animarkode.lida.feature.edit

import com.animarkode.lida.data.model.Kind

data class EditUiState(
    val isNew: Boolean = true,
    val title: String = "",
    val kind: Kind = Kind.SERIES,
    val season: String = "",
    val episode: String = "",
    val timestamp: String = "",
    val titleError: Boolean = false,
    val timestampError: Boolean = false,
    val isSaved: Boolean = false,
)
