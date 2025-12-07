package com.example.notificationapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notificationapp.MainActivity.Companion.NOTIFICATION_MODEL
import com.example.notificationapp.databinding.ActivityDetailBinding
import com.example.notificationapp.model.NotificationModel
import com.example.notificationapp.notif.NotificationHelper

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = IntentCompat.getParcelableExtra(intent, NOTIFICATION_MODEL, NotificationModel::class.java)
        val replyText = intent.getStringExtra(NotificationHelper.KEY_TEXT_REPLY)

        val titleText = item?.title ?: "ข้อความตอบกลับ"
        val reply = replyText ?: "ยังไม่มีข้อความตอบกลับ"

        binding.notifyDetailTv.text = "แจ้งเตือน ${titleText} ?: $reply"

    }
}