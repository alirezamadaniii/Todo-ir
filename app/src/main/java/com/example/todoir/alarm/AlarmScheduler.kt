package com.example.todoir.alarm

import com.example.todoir.data.model.Alarm


interface AlarmScheduler {

    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)
}