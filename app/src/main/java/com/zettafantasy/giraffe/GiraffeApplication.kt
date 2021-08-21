package com.zettafantasy.giraffe

import android.app.Application
import android.os.Build
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.GiraffeRoomDatabase
import com.zettafantasy.giraffe.feature.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GiraffeApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { GiraffeRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { GiraffeRepository(database.recordDao()) }

    override fun onCreate() {
        super.onCreate()
        Preferences.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChannel(baseContext)
        }
    }
}