package com.zettafantasy.giraffe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zettafantasy.giraffe.data.GiraffeRoomDatabase
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.data.RecordDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordDaoTest {

    private lateinit var recordDao: RecordDao
    private lateinit var db: GiraffeRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, GiraffeRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recordDao = db.recordDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRecord() = runBlocking {
        val record1 = Record(listOf(0, 1), listOf(1, 2), "stimulus")
        val id = recordDao.insert(record1)
        val record2 = recordDao.findRecord(id).first()
        assertNotNull(record2.id)
        assertEquals(record1.needIds, record2.needIds)
        assertEquals(record1.emotionIds, record2.emotionIds)
        assertEquals(record1.date, record2.date)
        assertEquals(record1.stimulus, record2.stimulus)
    }
}