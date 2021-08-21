package com.zettafantasy.giraffe.feature.notification

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
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.MainActivity
import com.zettafantasy.giraffe.R

object NotificationHelper {

    fun notifyRemindAlarm(context: Context) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        var builder = NotificationCompat.Builder(
            context,
            GiraffeConstant.NOTIFICATION_CHANNEL_ID_REMIND_ALARM
        )
            .setSmallIcon(R.drawable.ic_self_improvement_24dp)
            .setColor(ContextCompat.getColor(context, R.color.accent))
            .setContentTitle(context.getString(R.string.remind_alarm_title))
            .setContentText(context.getString(R.string.remind_alarm_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
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
        val importance = NotificationManager.IMPORTANCE_DEFAULT

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