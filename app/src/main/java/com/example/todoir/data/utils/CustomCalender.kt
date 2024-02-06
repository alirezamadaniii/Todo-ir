package com.example.todoir.data.utils

import android.util.SparseIntArray
import com.aminography.primedatepicker.common.BackgroundShapeType
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.example.todoir.R
import java.util.Calendar

class CustomCalender: LightThemeFactory() {

        override val typefacePath: String?
            get() = "fonts/vazir_bold.ttf"

        override val dialogBackgroundColor: Int
            get() = getColor(R.color.gray_light)

        override val calendarViewBackgroundColor: Int
            get() = getColor(R.color.gray_light)


        override val calendarViewPickedDayBackgroundColor: Int
            get() = getColor(R.color.purple)



        override val calendarViewDayLabelTextColor: Int
            get() = getColor(R.color.white)

        override val calendarViewTodayLabelTextColor: Int
            get() = getColor(R.color.purple)

        override val calendarViewWeekLabelFormatter: LabelFormatter
            get() = { primeCalendar ->
                when (primeCalendar[Calendar.DAY_OF_WEEK]) {
                    Calendar.THURSDAY,
                    Calendar.FRIDAY -> String.format("%s", primeCalendar.weekDayNameShort)
                    else -> String.format("%s", primeCalendar.weekDayNameShort)
                }
            }

        override val calendarViewWeekLabelTextColors: SparseIntArray
            get() = SparseIntArray(7).apply {
                val red = getColor(com.aminography.primedatepicker.R.color.red500)
                val indigo = getColor(com.aminography.primedatepicker.R.color.blue400)
                put(Calendar.SATURDAY, indigo)
                put(Calendar.SUNDAY, indigo)
                put(Calendar.MONDAY, indigo)
                put(Calendar.TUESDAY, indigo)
                put(Calendar.WEDNESDAY, indigo)
                put(Calendar.THURSDAY, red)
                put(Calendar.FRIDAY, red)
            }

        override val calendarViewShowAdjacentMonthDays: Boolean
            get() = true

        override val selectionBarBackgroundColor: Int
            get() = getColor(R.color.purple)

        override val actionBarTodayTextColor: Int
            get() = getColor(R.color.purple)


        override val pickedDayBackgroundShapeType: BackgroundShapeType
            get() = BackgroundShapeType.ROUND_SQUARE

}