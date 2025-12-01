package com.example.notificationapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationModel(
    val itemId: Int,
    val title: String,
    val message: String
): Parcelable
