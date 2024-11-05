package com.example.todoir.data.utils

import android.util.Log
import com.aminography.primecalendar.persian.PersianCalendar
import saman.zamani.persiandate.PersianDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.Date
import java.util.Locale
import java.util.TimeZone


data class DayInfo(val dayOfWeek: String, val date: LocalDate)
class CurrentWeek() {

    var strWeekDay: String = ""
    var strMonth: String = ""

    var date: Int = 0
    var month: Int = 0
    var year: Int = 0
    val currentDate: LocalDate = LocalDate.now()
    private val weekFields: WeekFields = WeekFields.of(Locale.getDefault())

    // Getting the week number of the year
    val weekNumber: Int = currentDate.get(weekFields.weekOfWeekBasedYear())

    // Getting the start of the week
    val startOfWeek: LocalDate = currentDate.minusDays(3) // 1 for Monday

    // Getting the end of the week
    val endOfWeek: LocalDate = startOfWeek.plusDays(3)

    // Getting the current date and time
    val currentDateTime: LocalDateTime = LocalDateTime.now()

    val monthName: String = currentDate.month.name

    val yearName: String = currentDate.year.toString()

    val currentName: String = currentDate.dayOfWeek.name




    // Method to display the days of the current week
    fun getDaysOfCurrentWeek(): List<DayInfo> {
        return (0..6).map {
            val date = startOfWeek.plusDays(it.toLong())
            DayInfo(dayOfWeek = date.dayOfWeek.name, date = date)
        }
    }

        // Method to display information about the current week
    fun displayCurrentWeekInfo(){
        var dayInfo :DayInfo
        val persianDate = PersianDate()
        Log.i("TAG", "Current Date: $currentDate")
        Log.i("TAG", "Week Number: $weekNumber")
        Log.i("TAG", "Start of the Week: $startOfWeek")
        Log.i("TAG", "End of the Week: $endOfWeek")
        Log.i("TAG", "Current Date and Time: $currentDateTime")

    }

    fun getYearAndMonth():String{
        return "$monthName $yearName"
    }



}