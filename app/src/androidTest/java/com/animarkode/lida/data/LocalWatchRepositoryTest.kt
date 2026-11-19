package com.animarkode.lida.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.animarkode.lida.data.local.AppDatabase
import com.animarkode.lida.data.model.EventType
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalWatchRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repo: LocalWatchRepository
    private var now = 0L

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        repo = LocalWatchRepository(db, clock = { now })
    }

    @After
    fun tearDown() = db.close()

    private fun series(season: Int? = null, episode: Int? = null) =
        WatchItem(title = "Severance", kind = Kind.SERIES, season = season, episode = episode, updatedAt = 0)

    @Test
    fun incrementEpisode_bumpsEpisodeAndWritesEvent() = runTest {
        val item = series(season = 2, episode = 5)
        repo.save(item)
        now = 100
        repo.incrementEpisode(item.id)

        val saved = repo.getItem(item.id)!!
        assertEquals(6, saved.episode)
        assertEquals(2, saved.season)
        assertEquals(100L, saved.updatedAt)
        val events = db.watchEventDao().getForItem(item.id)
        assertEquals(listOf(EventType.EPISODE_WATCHED), events.map { it.type })
        assertEquals(100L, events[0].at)
    }

    @Test
    fun incrementEpisode_fromNullStartsAtSeason1Episode1() = runTest {
        val item = series()
        repo.save(item)
        repo.incrementEpisode(item.id)

        val saved = repo.getItem(item.id)!!
        assertEquals(1, saved.episode)
        assertEquals(1, saved.season)
    }

    @Test
    fun incrementEpisode_onDeletedIsNoOp() = runTest {
        val item = series(season = 1, episode = 1)
        repo.save(item)
        repo.delete(item.id)
        repo.incrementEpisode(item.id)

        assertTrue(db.watchEventDao().getForItem(item.id).isEmpty())
    }

    @Test
    fun save_setsUpdatedAtFromClock() = runTest {
        val item = series()
        now = 42
        repo.save(item)
        assertEquals(42L, repo.getItem(item.id)!!.updatedAt)
    }

    @Test
    fun delete_hidesFromObserveItems() = runTest {
        val item = series()
        repo.save(item)
        repo.delete(item.id)
        assertTrue(repo.observeItems().first().isEmpty())
    }
}
