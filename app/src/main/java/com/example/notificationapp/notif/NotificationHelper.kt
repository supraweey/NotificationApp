package com.example.notificationapp.notif

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.example.notificationapp.DetailActivity
import com.example.notificationapp.NotificationActionReceiver
import com.example.notificationapp.R

object NotificationHelper {
    const val CHANNEL_GENERAL = "channel_general_high"

    const val ACTION_MARK_DONE = "com.example.notificationapp.ACTION_MARK_DONE"
    const val ACTION_INLINE_REPLY = "com.example.notificationapp.ACTION_INLINE_REPLY"
    const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    const val KEY_TEXT_REPLY = "key_text_reply"

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

    fun createMarkDonePendingIntent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_MARK_DONE
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        return PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }

    fun createInlineReplyAction(
        context: Context,
        notificationId: Int
    ): NotificationCompat.Action {
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel("พิมพ์คำตอบของคุณ...")
            .build()
        val replyIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_INLINE_REPLY
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }

        val replyFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            replyFlag
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_reply,
            "ตอบกลับ",
            replyPendingIntent
        ).addRemoteInput(remoteInput).setAllowGeneratedReplies(true).build()
    }

    fun buildGeneralNotification(
        context: Context,
        title: String,
        message: String,
        requestCode: Int = 1001,
        destinationIntent: Intent? = null,
        autoCancel: Boolean = true
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_stat_medicine)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(autoCancel)

        if (destinationIntent != null) {
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                destinationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)
        }

        return builder

    }

    fun notify(context: Context, id: Int, builder: NotificationCompat.Builder) {
        val nm = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            nm.notify(id, builder.build())
        }
    }

}