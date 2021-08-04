package com.zettafantasy.giraffe.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Record(
    val emotionIds: List<Int>, val needIds: List<Int>, val stimulus: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(index = true)
    var date: Date = Date()
}
