package com.zettafantasy.giraffe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val emotionIds: List<Int>,
    val needIds: List<Int>, @ColumnInfo(index = true) val date: Date
)
