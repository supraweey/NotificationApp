package com.example.notificationapp

import android.content.Intent
import android.util.Log
import com.example.notificationapp.model.NotificationModel
import com.example.notificationapp.notif.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "data = ${message.data}")
        Log.d("FCM", "notification = ${message.notification}")

        val data = message.data
        val itemId = data["itemId"]?.toIntOrNull() ?: 0

        val title = message.data["title"] ?: message.notification?.title ?: "FCM Message"
        val message = message.data["message"] ?: message.notification?.body ?: "No message"
        val model = NotificationModel(itemId, title, message)
        val detailIntent = Intent(this, DetailActivity::class.java).apply {
            putExtra(MainActivity.NOTIFICATION_MODEL, model)
        }
        val builder = NotificationHelper.buildGeneralNotification(
            context = this,
            title = title,
            message = message,
            requestCode = itemId,
            destinationIntent = detailIntent,
            autoCancel = true
        )

        NotificationHelper.notify(this, id = 9999, builder)
    }
}