package com.example.todoir.peresentaion.ui

import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.civil.CivilCalendarUtils
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.utils.CurrentWeek
import com.example.todoir.data.utils.CustomCalender
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone


class CalenderFragment : Fragment() {

    private val customCalender = CustomCalender()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentWeek = CurrentWeek()
        currentWeek.displayCurrentWeekInfo()
        persianCalender()
    }

    private fun persianCalender() {
        val today = CivilCalendar(TimeZone.getTimeZone("GMT+3:30"))


        Log.i("TAG", "persianCalendedfsfdsfr: ${today.weekOfYear} \n ${today.weekOfMonth} \n" +
                " ${today.weekDayName}\n" +
                " ${today.weekDayNameShort}\n" +
                " ${today.dayOfWeek}\n" +
                " ${today.firstDayOfWeek}\n" +
                " ${today.dayOfWeekInMonth}")

        val calendar = PersianCalendar(TimeZone.getTimeZone("GMT+4:30"))


        Log.i("TAG", "persianCalendedfsfdsfr22:  \n ${calendar.weekOfMonth} \n" +
                " ${calendar.weekDayName}\n" +
                " ${calendar.weekDayNameShort}\n" +
                " ${calendar.dayOfWeek}\n" +
                " ${calendar.firstDayOfWeek}\n" +
                " ${calendar.dayOfWeekInMonth}")

       val a= android.icu.util.Calendar.getWeekDataForRegion("Tehran")


        Log.i("TAG", "persianCalender2: "+a.weekendOnset.toString())

        val callback = SingleDayPickCallback { day ->


        }

        val datePicker = PrimeDatePicker.dialogWith(calendar)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(calendar)
            .applyTheme(customCalender)
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")



    }

}