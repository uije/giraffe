package com.zettafantasy.giraffe.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class Converters {

    private val gson = Gson()

    @TypeConverter
    fun toListInt(str: String): List<Int> {
        return gson.fromJson(str, object : TypeToken<List<Int>>() {}.type)
    }

    @TypeConverter
    fun fromListInt(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toDate(date: Long) = Date(date)

    @TypeConverter
    fun fromDate(date: Date) = date.time
}