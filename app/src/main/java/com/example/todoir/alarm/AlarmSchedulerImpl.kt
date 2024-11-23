//package com.example.todoir.alarm
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import com.example.todoir.data.model.Alarm
//import com.example.todoir.data.utils.Constants.ALARM_ID
//import com.example.todoir.data.utils.Constants.MESSAGE
//import com.example.todoir.data.utils.Constants.TITLE
//import com.example.todoir.receiver.com.example.todoir.receiver.AlarmReceiver
//
//
//class AlarmSchedulerImpl(private val context: Context) : AlarmScheduler {
//
//    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//    override fun schedule(alarm: Alarm) {
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            alarm.hashCode(),
//            Intent(context, com.example.todoir.receiver.AlarmReceiver::class.java).apply {
//                putExtra(TITLE, alarm.title)
//                putExtra(MESSAGE, alarm.message)
//                putExtra(ALARM_ID, alarm.id)
//            },
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                alarm.scheduleAt,
//                pendingIntent
//            )
//        }else{
//            alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                alarm.scheduleAt,
//                pendingIntent
//            )
//        }
//    }
//
//    override fun cancel(alarm: Alarm) {
//        alarmManager.cancel(
//            PendingIntent.getBroadcast(
//                context,
//                alarm.hashCode(),
//                Intent(context, com.example.todoir.receiver.AlarmReceiver::class.java),
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )
//    }
//
//}