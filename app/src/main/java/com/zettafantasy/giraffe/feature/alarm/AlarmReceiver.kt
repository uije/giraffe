package com.zettafantasy.giraffe.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.common.Preferences
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(javaClass.simpleName, "onReceive")

        context?.run {
            if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
                // 재부팅시 알람 재등록
                GiraffeAlarmManager.init(context)
            } else if (isNotUsedRecently()) {
                NotificationHelper.notifyRemindAlarm(this)
            }
        }
    }

    private fun isNotUsedRecently(): Boolean {
        val sinceLastUsedHours =
            TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - Preferences.lastUsedTime)
        Log.d(javaClass.simpleName, String.format("hours since last used : %s", sinceLastUsedHours))
        return sinceLastUsedHours > GiraffeConstant.ALARM_NOT_USED_HOURS
    }

}