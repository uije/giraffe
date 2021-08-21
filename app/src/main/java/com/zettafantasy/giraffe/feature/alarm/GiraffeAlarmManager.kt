package com.zettafantasy.giraffe.feature.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.content.ContextCompat

object GiraffeAlarmManager {
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    fun init(context: Context) {
        alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        alarmManager?.cancel(alarmIntent)
        Log.d(javaClass.simpleName, "alarm was canceled")

        alarmManager?.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10 * 1000, alarmIntent //todo
        )
        Log.d(javaClass.simpleName, "alarm registered")
    }
}