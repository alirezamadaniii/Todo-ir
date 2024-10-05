package com.example.todoir.peresentaion.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.civil.CivilCalendarUtils
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.utils.CustomCalender


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
        englishCalender()
    }

    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            val month = (day.month+1)
//            date = "${day.year} / $month /  ${day.date}"
            Log.i("TAG", "persianCalender: " + day.year + "/" + month + "/" + day.date)
//            timePiker()
        }

        val today = CivilCalendar()
        Log.i("TAG", "englishCalender: "+today.month)


        val datePicker = PrimeDatePicker.dialogWith(today!!)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today!!)
            .applyTheme(customCalender)
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG") }

}