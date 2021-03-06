package com.zettafantasy.giraffe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM record ORDER BY date DESC")
    fun getRecords(): Flow<List<Record>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: Record)

    @Query("DELETE FROM record")
    suspend fun deleteAll()
}