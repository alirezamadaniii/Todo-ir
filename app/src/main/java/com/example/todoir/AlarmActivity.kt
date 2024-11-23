package com.example.todoir

import android.annotation.SuppressLint
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AlarmActivity : AppCompatActivity() {

    private lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // Get the default alarm sound URI
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        // Initialize the Ringtone
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmSound)
        ringtone.play() // Start playing the alarm sound

        // Dismiss button functionality
        val dismissButton: Button = findViewById(R.id.dismiss_button)
        dismissButton.setOnClickListener {
            stopAlarm() // Stop the alarm sound
            finish() // Close the activity
        }
    }

    private fun stopAlarm() {
        if (ringtone.isPlaying) {
            ringtone.stop() // Stop the sound if it's playing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm() // Ensure sound is stopped when the activity is destroyed
    }
}