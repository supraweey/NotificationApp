package com.example.notificationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.example.notificationapp.notif.NotificationHelper

class NotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, 0)

        when(intent?.action){
            NotificationHelper.ACTION_MARK_DONE -> {
                Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show()
                notificationId?.let {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }

        }

    }
}