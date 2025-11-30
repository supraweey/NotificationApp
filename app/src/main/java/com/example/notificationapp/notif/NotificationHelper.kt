package com.example.notificationapp.notif

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationapp.MainActivity
import com.example.notificationapp.R

object NotificationHelper {
    const val CHANNEL_GENERAL = "channel_general_high"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                CHANNEL_GENERAL, context.getString(R.string.channel_general_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_general_desc)
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.RED
            }
            manager.createNotificationChannel(channel)
        }
    }

}