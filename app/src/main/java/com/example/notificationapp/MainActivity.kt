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
            showAllDetailNotification()
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
            showAllDetailNotification()
        }
    }

    private fun showAllDetailNotification(){
        showDetailNotification(NotificationModel(itemId = 101, title = "แจ้งเตือน ครั้งที่ 1", message = "รายละเอียด"))
        showDetailNotification(NotificationModel(itemId = 202, title = "แจ้งเตือน ครั้งที่ 2", message = "รายละเอียด2"))
    }

    private fun showDetailNotification(notificationModel: NotificationModel){
        val detailActivity = Intent(this, DetailActivity::class.java).apply {
            putExtra(NOTIFICATION_MODEL, notificationModel)
        }
        val pendingIntent = PendingIntent.getActivity(this, notificationModel.itemId, detailActivity, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_stat_medicine)
            .setContentTitle(title)
            .setContentText(notificationModel.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(notificationModel.itemId, builder.build())
        }
    }

    companion object {
        const val NOTIFICATION_MODEL = "notificationModel"
    }
}