package com.example.notificationapp.notif

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationapp.MainActivity
import com.example.notificationapp.R

object NotificationHelper {
    const val CHANNEL_ID = "app_default_channel"

    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSampleDataNotification(
        context: Context, title: String, message: String,
        bigText: String, autoCancel: Boolean
    ) {

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_stat_medicine) // 3
            setContentTitle(title) // 4
            setContentText(message) // 5
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText)) // 6
            priority = NotificationCompat.PRIORITY_MAX // 7
            setAutoCancel(autoCancel) // 8

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1001, notificationBuilder.build())
        }

    }
}