package com.animarkode.lida

import android.content.Context
import com.animarkode.lida.data.LocalWatchRepository
import com.animarkode.lida.data.WatchRepository
import com.animarkode.lida.data.local.AppDatabase

/** Manual DI: app-wide singletons live here. */
class AppContainer(context: Context) {
    private val database = AppDatabase.build(context)

    val watchRepository: WatchRepository = LocalWatchRepository(database)
}
