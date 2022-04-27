package com.zettafantasy.giraffe.feature.remind

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.MainActivity
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.DestinationScreen
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object NotificationHelper {

    fun notifyRemindAlarm(context: Context) {
        val repository = (context.applicationContext as GiraffeApplication).repository

        CoroutineScope(Dispatchers.IO).launch {
            val records =
                repository.findRecordsSince(Preferences.wordCloudPeriod.getTime()).firstOrNull()
            val remindNotificationType = getRemindNotificationType(records)
            val notification = createNotification(
                context,
                title = remindNotificationType.getDecoratedTitle(context, records),
                text = context.resources.getString(remindNotificationType.text)
            )
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(0, notification)
            }
            Preferences.lastRemindType = remindNotificationType
        }
    }

    private fun createNotification(context: Context, title: String, text: String): Notification {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(GiraffeConstant.EXTRA_KEY_SCREEN_DESTINATION, DestinationScreen.RECORD)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(
            context,
            GiraffeConstant.NOTIFICATION_CHANNEL_ID_REMIND_ALARM
        )
            .setSmallIcon(R.drawable.ic_self_improvement_24dp)
            .setColor(ContextCompat.getColor(context, R.color.accent))
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //lock screen 노출
            .build()
    }

    private fun getRemindNotificationType(records: List<Record>?): RemindNotificationType {
        if (records.isNullOrEmpty() || Preferences.defaultScreen == GiraffeConstant.SCREEN_RECORD) {
            return RemindNotificationType.DEFAULT
        }

        return when (Preferences.lastRemindType) {
            RemindNotificationType.DEFAULT -> RemindNotificationType.EMOTION
            RemindNotificationType.EMOTION -> RemindNotificationType.NEED
            RemindNotificationType.NEED -> RemindNotificationType.DEFAULT
        }
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because
     * the NotificationChannel class is new and not in the support library
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.notification_channel_remind_alarm_name)
        val descriptionText = context.getString(R.string.notification_channel_remind_alarm_desc)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(
            GiraffeConstant.NOTIFICATION_CHANNEL_ID_REMIND_ALARM,
            name,
            importance
        ).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}