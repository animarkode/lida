package com.animarkode.lida.testing

import com.animarkode.lida.data.WatchRepository
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory [WatchRepository] mirroring LocalWatchRepository's rules. */
class FakeWatchRepository(initial: List<WatchItem> = emptyList()) : WatchRepository {
    private var now = 0L
    private val all = MutableStateFlow(initial)

    /** Every stored row, deleted ones included. */
    val rows: List<WatchItem> get() = all.value

    override fun observeItems(): Flow<List<WatchItem>> =
        all.map { list -> list.filterNot { it.deleted }.sortedByDescending { it.updatedAt } }

    override suspend fun getItem(id: String): WatchItem? =
        all.value.firstOrNull { it.id == id && !it.deleted }

    override suspend fun save(item: WatchItem) = put(item.copy(updatedAt = ++now))

    override suspend fun incrementEpisode(id: String) {
        val item = getItem(id) ?: return
        put(item.copy(season = item.season ?: 1, episode = (item.episode ?: 0) + 1, updatedAt = ++now))
    }

    override suspend fun delete(id: String) {
        val item = getItem(id) ?: return
        put(item.copy(deleted = true, updatedAt = ++now))
    }

    private fun put(item: WatchItem) = all.update { list -> list.filterNot { it.id == item.id } + item }
}
