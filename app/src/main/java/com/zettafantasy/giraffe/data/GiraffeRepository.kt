package com.zettafantasy.giraffe.data

import androidx.annotation.WorkerThread
import com.zettafantasy.giraffe.model.Need
import kotlinx.coroutines.flow.Flow

class GiraffeRepository(private val needDao: NeedDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allNeeds: Flow<List<Need>> = needDao.getNeeds()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(need: Need) {
        needDao.insert(need)
    }
}
