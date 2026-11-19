package com.animarkode.lida.data

import androidx.room.withTransaction
import com.animarkode.lida.data.local.AppDatabase
import com.animarkode.lida.data.model.EventType
import com.animarkode.lida.data.model.WatchEvent
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.Flow

class LocalWatchRepository(
    private val db: AppDatabase,
    private val clock: () -> Long = System::currentTimeMillis,
) : WatchRepository {
    private val items = db.watchItemDao()
    private val events = db.watchEventDao()

    override fun observeItems(): Flow<List<WatchItem>> = items.observeActive()

    override suspend fun getItem(id: String): WatchItem? = items.getActive(id)

    override suspend fun save(item: WatchItem) = items.upsert(item.copy(updatedAt = clock()))

    override suspend fun incrementEpisode(id: String) = db.withTransaction {
        val item = items.getActive(id) ?: return@withTransaction
        val now = clock()
        items.upsert(
            item.copy(
                season = item.season ?: 1,
                episode = (item.episode ?: 0) + 1,
                timestamp = null, // the position belonged to the previous episode
                updatedAt = now,
            ),
        )
        events.insert(WatchEvent(itemId = id, at = now, type = EventType.EPISODE_WATCHED))
    }

    override suspend fun delete(id: String) {
        val item = items.getActive(id) ?: return
        items.upsert(item.copy(deleted = true, updatedAt = clock()))
    }
}
