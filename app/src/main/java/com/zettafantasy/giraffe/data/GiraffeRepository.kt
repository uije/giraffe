package com.zettafantasy.giraffe.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GiraffeRepository(private val recordDao: RecordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allRecords: Flow<List<Record>> = recordDao.getRecords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(record: Record) {
        recordDao.insert(record)
    }
}
