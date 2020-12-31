package com.zettafantasy.giraffe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zettafantasy.giraffe.model.Need
import kotlinx.coroutines.flow.Flow

//@Dao
interface NeedDao {
    @Query("SELECT * FROM need")
    fun getNeeds(): Flow<List<Need>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(need: Need)

    @Query("DELETE FROM need")
    suspend fun deleteAll()
}