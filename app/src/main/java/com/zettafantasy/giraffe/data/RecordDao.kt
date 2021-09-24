package com.zettafantasy.giraffe.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM record ORDER BY date DESC")
    fun getRecords(): Flow<List<Record>>

    @Query("SELECT * FROM record WHERE id == :id")
    fun findRecord(id: Long): Flow<Record>

    @Query("SELECT * FROM record WHERE date >= :date")
    fun findRecordsSince(date: Long): Flow<List<Record>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: Record)

    @Query("DELETE FROM record")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(record: Record)
}