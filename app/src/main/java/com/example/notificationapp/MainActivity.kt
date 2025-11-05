package com.example.notificationapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notificationapp.databinding.ActivityMainBinding
import com.example.notificationapp.notif.NotificationHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestPostNotifications =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                showSampleNotification()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.notifyTv.setOnClickListener {
            registerNotification()
        }
    }

    private fun registerNotification() {
        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH, false,
            getString(R.string.app_name), "App notification channel."
        )

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showSampleNotification()
            } else {
                requestPostNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            showSampleNotification()
        }
    }

    private fun showSampleNotification() {
        NotificationHelper.createSampleDataNotification(
            this,
            title = "แจ้งเตือนตัวอย่าง",
            message = "สวัสดีจาก NotificationHelper",
            bigText = "นี่คือตัวอย่างการแจ้งเตือนแบบ BigText ที่ใช้ View Binding และรองรับ Android 13+",
            autoCancel = true
        )
    }
}