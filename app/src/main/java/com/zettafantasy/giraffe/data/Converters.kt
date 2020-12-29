package com.zettafantasy.giraffe.data

import androidx.room.TypeConverter
import com.zettafantasy.giraffe.model.Need

class Converters {
    @TypeConverter
    fun toNeedType(value: Int) = enumValues<Need.NeedType>()[value]

    @TypeConverter
    fun fromNeedType(value: Need.NeedType) = value.ordinal
}