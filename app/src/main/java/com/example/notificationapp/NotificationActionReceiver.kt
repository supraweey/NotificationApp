package com.example.notificationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
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

            NotificationHelper.ACTION_INLINE_REPLY -> {
                val replyText = getReplyText(intent)
                Toast.makeText(context, "Reply : $replyText", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun getReplyText(intent: Intent): String {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        return remoteInput?.getCharSequence(NotificationHelper.KEY_TEXT_REPLY).toString()

    }

}