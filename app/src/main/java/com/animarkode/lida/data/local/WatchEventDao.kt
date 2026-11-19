package com.animarkode.lida.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.animarkode.lida.data.model.WatchEvent

@Dao
interface WatchEventDao {
    @Insert
    suspend fun insert(event: WatchEvent)

    @Query("SELECT * FROM WatchEvent WHERE itemId = :itemId ORDER BY at")
    suspend fun getForItem(itemId: String): List<WatchEvent>
}
