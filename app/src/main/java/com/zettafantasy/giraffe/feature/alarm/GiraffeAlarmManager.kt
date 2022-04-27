package com.zettafantasy.giraffe.feature.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

object GiraffeAlarmManager {
    private var alarmManager: AlarmManager? = null
    private var alarmIntent: PendingIntent? = null
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA)

    fun init(context: Context) {
        alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 21) //todo 시간 설정값으로 빼기
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
        }

        //목표시간 이후라면 재설정하지 않는다
        if (Calendar.getInstance().after(calendar)) {
            Log.d(javaClass.simpleName, String.format("alarm register was skipped"))
            return
        }

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

//        alarmManager?.set(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 10 * 1000, alarmIntent
//        )

        Log.d(
            javaClass.simpleName,
            String.format("alarm registered %s", sdf.format(calendar.time))
        )
    }
}