package com.zettafantasy.giraffe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Record(
    val emotionIds: List<Int>, val needIds: List<Int>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(index = true)
    var date: Date = Date()
}
