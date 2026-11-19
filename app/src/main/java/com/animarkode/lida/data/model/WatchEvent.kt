package com.animarkode.lida.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(indices = [Index("itemId")])
data class WatchEvent(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val itemId: String,
    val at: Long,
    val type: EventType,
)
