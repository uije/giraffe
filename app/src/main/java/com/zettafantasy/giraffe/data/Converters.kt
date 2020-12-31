package com.zettafantasy.giraffe.data

import androidx.room.TypeConverter
import com.zettafantasy.giraffe.model.NeedType

class Converters {
    @TypeConverter
    fun toNeedType(value: Int) = enumValues<NeedType>()[value]

    @TypeConverter
    fun fromNeedType(value: NeedType) = value.ordinal
}