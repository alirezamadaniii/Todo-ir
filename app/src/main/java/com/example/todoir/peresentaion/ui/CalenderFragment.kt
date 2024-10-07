package com.example.todoir.peresentaion.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.utils.CurrentWeek
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.databinding.FragmentCalenderBinding
import com.example.todoir.peresentaion.adapter.WeekAdapter


class CalenderFragment : Fragment() {

    private lateinit var binding: FragmentCalenderBinding
    private val customCalender = CustomCalender()
    private val currentWeek = CurrentWeek()
    private  var dayInfoList = mutableListOf<DayInfo>()
    private lateinit var adapter: WeekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_calender, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentWeek = CurrentWeek()
        currentWeek.displayCurrentWeekInfo()
        displayCurrentWeekInfo()
        setUpEnglishCalendar()
    }



    private fun setUpEnglishCalendar() {
        val monthName=currentWeek.getYearAndMonth()
        binding.tvMonthCalendar.text = monthName
        binding.btnShowCalendar.setOnClickListener {
            englishCalender()
        }
    }

    private fun displayCurrentWeekInfo() {
            currentWeek.getDaysOfCurrentWeek().forEach { day ->
                dayInfoList.add(day)
                Log.i("SSSAASS", "${day.dayOfWeek} - ${day.date}")
        }

        adapter = WeekAdapter()
        adapter.differ.submitList(dayInfoList)
        binding.recyWeek.layoutManager = GridLayoutManager(requireContext(),7)
        binding.recyWeek.adapter = adapter

    }


    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            val month = (day.month+1)

        }

        val today = CivilCalendar()
        val datePicker = PrimeDatePicker.dialogWith(today)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today)
            .applyTheme(customCalender)
            .build()
        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")
    }


}