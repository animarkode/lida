package com.animarkode.lida.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.animarkode.lida.data.model.WatchEvent
import com.animarkode.lida.data.model.WatchItem

@Database(entities = [WatchItem::class, WatchEvent::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchItemDao(): WatchItemDao
    abstract fun watchEventDao(): WatchEventDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "lida.db").build()
    }
}
