package com.animarkode.lida.data

import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.Flow

interface WatchRepository {
    /** Active (not deleted) items, most recently updated first. */
    fun observeItems(): Flow<List<WatchItem>>

    suspend fun getItem(id: String): WatchItem?

    /** Inserts or replaces [item], stamping `updatedAt` with the current time. */
    suspend fun save(item: WatchItem)

    /** Bumps the episode and records an event; no-op for a missing or deleted item. */
    suspend fun incrementEpisode(id: String)

    /** Soft delete: marks the item as deleted. */
    suspend fun delete(id: String)
}
