package com.zettafantasy.giraffe.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Record::class], version = 2)
@TypeConverters(Converters::class)
abstract class GiraffeRoomDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: GiraffeRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): GiraffeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GiraffeRoomDatabase::class.java,
                    "giraffe_db"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .addCallback(GiraffeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class GiraffeDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.recordDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(this.javaClass.simpleName, "onOpen")
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(recordDao: RecordDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            Log.d(this.javaClass.simpleName, "populateDatabase")
        }
    }
}