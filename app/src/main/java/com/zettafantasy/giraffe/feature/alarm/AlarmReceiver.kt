package com.zettafantasy.giraffe.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(javaClass.simpleName, "onReceive")

        context?.run {
            if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
                // 재부팅시 알람 재등록
                GiraffeAlarmManager.init(context)
            } else {
                NotificationHelper.notifyRemindAlarm(this)
            }
        }
    }
}