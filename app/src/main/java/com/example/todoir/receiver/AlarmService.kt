package com.example.todoir.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.todoir.MainActivity
import com.example.todoir.R

class AlarmService : Service() {

    private val CHANNEL_ID = "AlarmServiceChannel"
    private var title :String =""
    private var destination :String =""

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i("TAG", "onStartCommand1: ")
        title = intent?.extras?.getString("title").toString()
        destination = intent?.extras?.getString("description").toString()
        sendNotification()
        return super.onStartCommand(intent, flags, startId)

    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun sendNotification() {
        Log.i("TAG", "onStartCommand2: ")
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(destination)
            .setSmallIcon(R.drawable.add_1) // Make sure to replace with your actual icon
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification) // Show the notification
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}