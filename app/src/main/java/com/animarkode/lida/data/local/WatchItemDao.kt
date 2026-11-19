package com.animarkode.lida.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.animarkode.lida.data.model.WatchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchItemDao {
    @Query("SELECT * FROM WatchItem WHERE deleted = 0 ORDER BY updatedAt DESC")
    fun observeActive(): Flow<List<WatchItem>>

    @Query("SELECT * FROM WatchItem WHERE id = :id AND deleted = 0")
    suspend fun getActive(id: String): WatchItem?

    @Upsert
    suspend fun upsert(item: WatchItem)
}
