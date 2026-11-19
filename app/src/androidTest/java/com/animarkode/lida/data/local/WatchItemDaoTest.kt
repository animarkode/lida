package com.animarkode.lida.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WatchItemDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: WatchItemDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        dao = db.watchItemDao()
    }

    @After
    fun tearDown() = db.close()

    private fun item(title: String, updatedAt: Long = 0, deleted: Boolean = false) =
        WatchItem(title = title, kind = Kind.MOVIE, updatedAt = updatedAt, deleted = deleted)

    @Test
    fun observeActive_sortsByUpdatedAtDesc() = runTest {
        dao.upsert(item("a", updatedAt = 1))
        dao.upsert(item("b", updatedAt = 3))
        dao.upsert(item("c", updatedAt = 2))
        assertEquals(listOf("b", "c", "a"), dao.observeActive().first().map { it.title })
    }

    @Test
    fun observeActive_hidesDeleted() = runTest {
        dao.upsert(item("x"))
        dao.upsert(item("y", deleted = true))
        assertEquals(listOf("x"), dao.observeActive().first().map { it.title })
    }

    @Test
    fun getActive_returnsNullForDeleted() = runTest {
        val deleted = item("gone", deleted = true)
        dao.upsert(deleted)
        assertNull(dao.getActive(deleted.id))
    }

    @Test
    fun upsert_replacesExistingById() = runTest {
        val original = item("A")
        dao.upsert(original)
        dao.upsert(original.copy(title = "B"))
        assertEquals(listOf("B"), dao.observeActive().first().map { it.title })
    }
}
