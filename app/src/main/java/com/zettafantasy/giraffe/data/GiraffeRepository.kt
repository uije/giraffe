package com.zettafantasy.giraffe.data

import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class GiraffeRepository(private val recordDao: RecordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    fun getPagingRecords(): PagingSource<Int, Record> = recordDao.getPagingRecords()

    fun getRecords(): List<Record> = recordDao.getRecords()

    fun findRecord(id: Long): Flow<Record> {
        return recordDao.findRecord(id)
    }

    fun findRecordsSince(date: Long): Flow<List<Record>> {
        return recordDao.findRecordsSince(date)
    }

    fun isRecordEmpty() = recordDao.isEmpty()

    suspend fun deleteRecord(record: Record) {
        return recordDao.delete(record)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(record: Record) = recordDao.insert(record)
}
