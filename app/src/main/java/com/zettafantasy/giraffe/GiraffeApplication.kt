package com.zettafantasy.giraffe

import android.app.Application
import com.zettafantasy.giraffe.data.GiraffeRoomDatabase
import com.zettafantasy.giraffe.data.GiraffeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GiraffeApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { GiraffeRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { GiraffeRepository(database.needDao()) }
}