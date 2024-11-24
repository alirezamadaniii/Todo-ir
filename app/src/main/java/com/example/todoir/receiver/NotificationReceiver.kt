package com.example.todoir.receiver
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.todoir.R

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val taskTitle = intent.getStringExtra("title")
        val taskDescription = intent.getStringExtra("description")

        // Start the AlarmService just to send the notification
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("title",taskTitle)
        serviceIntent.putExtra("description",taskDescription)
        context.startForegroundService(serviceIntent)
    }
}