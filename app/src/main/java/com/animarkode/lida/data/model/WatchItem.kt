package com.animarkode.lida.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class WatchItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val tmdbId: Int? = null,
    val kind: Kind,
    val status: Status = Status.WATCHING,
    val season: Int? = null,
    val episode: Int? = null,
    val timestamp: String? = null,
    val updatedAt: Long,
    val deleted: Boolean = false,
)
