package com.zettafantasy.giraffe.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(javaClass.simpleName, "onReceive")
        context?.run {
            NotificationHelper.notifyRemindAlarm(this)
        }
    }
}