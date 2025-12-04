package com.example.notificationapp

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationapp.databinding.ActivityMainBinding
import com.example.notificationapp.model.NotificationModel
import com.example.notificationapp.notif.NotificationHelper

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showChatNotification()
//            showAllDetailNotification()
        } else {
            Toast.makeText(this, "ไม่ได้ให้สิทธิแจ้งเตือน", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createNotificationChannel(this)

        binding.notifyDetailBtn.setOnClickListener { registerNotification() }
    }

    private fun registerNotification() {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showChatNotification()
//            showAllDetailNotification()
        }
    }

    private fun showAllDetailNotification() {
        showDetailNotification(
            NotificationModel(
                itemId = 101,
                title = "แจ้งเตือน ครั้งที่ 1",
                message = "รายละเอียด"
            )
        )
        showDetailNotification(
            NotificationModel(
                itemId = 202,
                title = "แจ้งเตือน ครั้งที่ 2",
                message = "รายละเอียด2"
            )
        )
    }

    private fun showDetailNotification(notificationModel: NotificationModel) {
        val detailActivity = Intent(this, DetailActivity::class.java).apply {
            putExtra(NOTIFICATION_MODEL, notificationModel)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationModel.itemId,
            detailActivity,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val markDonePendingIntent =
            NotificationHelper.createMarkDonePendingIntent(this, notificationModel.itemId)

        val inlineReplyAction =
            NotificationHelper.createInlineReplyAction(this, notificationModel.itemId)

        val builder = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_stat_medicine)
            .setContentTitle(title)
            .setContentText(notificationModel.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(inlineReplyAction)
//            .addAction(R.drawable.ic_check_circle, "Delete", markDonePendingIntent)
        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationModel.itemId, builder.build())
        }
    }

    private fun showChatNotification() {
        val notificationId = 2001
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val inlineReplyAction = NotificationHelper.createInlineReplyAction(this, notificationId)

        val builder = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_stat_medicine)
            .setContentTitle(title)
            .setContentText("Question ?")
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .addAction(inlineReplyAction)
        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        }

    }

    companion object {
        const val NOTIFICATION_MODEL = "notificationModel"
    }
}