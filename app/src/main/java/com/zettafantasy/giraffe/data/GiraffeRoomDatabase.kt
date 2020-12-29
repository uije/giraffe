package com.zettafantasy.giraffe.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zettafantasy.giraffe.model.Need

@Database(entities = [Need::class], version = 1)
abstract class GiraffeRoomDatabase : RoomDatabase() {

    abstract fun desireDao(): NeedDao
}