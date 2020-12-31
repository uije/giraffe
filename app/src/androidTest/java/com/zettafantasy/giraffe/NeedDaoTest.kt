package com.zettafantasy.giraffe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zettafantasy.giraffe.data.NeedDao
import com.zettafantasy.giraffe.data.GiraffeRoomDatabase
import com.zettafantasy.giraffe.model.Need
import com.zettafantasy.giraffe.model.NeedType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class NeedDaoTest {

    private lateinit var needDao: NeedDao
    private lateinit var db: GiraffeRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, GiraffeRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        needDao = db.needDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val need = Need(null, "aaa", NeedType.AUTONOMY)
        needDao.insert(need)
        val allNeeds = needDao.getNeeds().first()
        assertEquals(allNeeds[0].getName(), need.getName())
        assertEquals(allNeeds[0].type, need.type)
    }
}