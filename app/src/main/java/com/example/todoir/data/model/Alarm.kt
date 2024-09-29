package com.example.todoir.data.model

import androidx.room.PrimaryKey

data class Alarm(
    var id:Long,
    val title: String,
    val message: String,
    val scheduleAt:Long
)

