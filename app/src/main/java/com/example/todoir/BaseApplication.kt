package com.example.todoir

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.todoir.data.utils.Constants.ALARM_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication:Application(){
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.reminder)
            val channelDescription = getString(R.string.reminder_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(ALARM_CHANNEL_NAME, name, importance)
            mChannel.description = channelDescription
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}
