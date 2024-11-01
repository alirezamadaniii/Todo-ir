package com.example.todoir.peresentaion.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.utils.CurrentWeek
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.DayInfo
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.FragmentCalenderBinding
import com.example.todoir.peresentaion.adapter.TimeLineCalendarAdapter
import com.example.todoir.peresentaion.adapter.WeekAdapter
import com.example.todoir.peresentaion.adapter.WeekPersianAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import saman.zamani.persiandate.PersianDate
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class CalenderFragment : Fragment() {

    private lateinit var binding: FragmentCalenderBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val customCalender = CustomCalender()
    private val currentWeek = CurrentWeek()
    private var date: String? = null
    private  var dayInfoEnglishList = mutableListOf<DayInfo>()
    private lateinit var englishAdapter: WeekAdapter
    private lateinit var persianAdapter: WeekPersianAdapter
    private lateinit var timeLineCalendarAdapter: TimeLineCalendarAdapter

    @Inject
    lateinit var sp: Sp

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

        chooseLangForCalender()

        binding.checkNight.addOnCheckedStateChangedListener { _, state ->
            if (state==1) binding.timelineLineNight.setBackgroundResource(R.drawable.dotted_line_selected)
        }

        timeLineCalendarAdapter.setOnCheckBoxClick { item, i ->
            if (i == 1) {
                viewModel.completedTask(item.taskId, true)
            } else {
                viewModel.completedTask(item.taskId, false)
            }
        }


        timeLineCalendarAdapter.setOnItemClick {
            val bundle = Bundle().apply {
                putParcelable("task",it)
            }
            findNavController().navigate(R.id.action_calenderFragment_to_updateFragment,bundle)
        }



    }


    private fun getDateFilter(date: String) {
        timeLineCalendarAdapter = TimeLineCalendarAdapter()
        viewModel.getTaskAfterFilter(date).observe(viewLifecycleOwner) {
            Log.i("TAG", "getDateFilter: "+it)
            binding.btnAddTaskCalander.visibility = View.GONE
            binding.recyCalender.visibility = View.VISIBLE
            binding.recyCalender.adapter = timeLineCalendarAdapter
            timeLineCalendarAdapter.differ.submitList(it)
        }
    }

    private fun chooseLangForCalender() {
        if (sp.fetch("language") == "Persian") {
            setUpPersianCalendar()
        } else {
        val currentWeek = CurrentWeek()
        currentWeek.displayCurrentWeekInfo()
        displayCurrentWeekInfo()
        setUpEnglishCalendar()
        }
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
                dayInfoEnglishList.add(day)
        }
        getDateFilter("${currentWeek.currentDate.year} / ${currentWeek.currentDate.monthValue} /  ${currentWeek.currentDate.dayOfMonth}")

        englishAdapter = WeekAdapter()
        englishAdapter.differ.submitList(dayInfoEnglishList)
        binding.recyWeek.layoutManager = GridLayoutManager(requireContext(),7)
        binding.recyWeek.adapter = englishAdapter

        englishAdapter.setOnItemClick {
            getDateFilter("${currentWeek.currentDate.year} / ${currentWeek.currentDate.monthValue} /  $it")
            Log.i("TAG", "displayCurrentWeekInfo: ${currentWeek.currentDate.year} / ${currentWeek.currentDate.monthValue} /  $it")
        }


    }

    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            val month = (day.month+1)
            date = "${day.year} / $month /  ${day.date}"
            getDateFilter(date!!)
        }

        val today = CivilCalendar()
        val datePicker = PrimeDatePicker.dialogWith(today)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today)
            .applyTheme(customCalender)
            .build()
        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")
    }



    private fun setUpPersianCalendar(){
        val persianDate = PersianDate()
        val monthName="${persianDate.monthName} ${persianDate.shYear}"
        binding.tvMonthCalendar.text = monthName
        binding.btnShowCalendar.setOnClickListener {
            persianCalender()
        }


        currentWeek.getDaysOfCurrentWeek().forEach { day ->
            dayInfoEnglishList.add(day)
        }

        persianAdapter = WeekPersianAdapter()
        persianAdapter.differ.submitList(dayInfoEnglishList)
        binding.recyWeek.layoutManager = GridLayoutManager(requireContext(),7)
        binding.recyWeek.adapter = persianAdapter
    }

    private fun persianCalender() {
        val calendar = PersianCalendar(TimeZone.getTimeZone("GMT+4:30"))
        val callback = SingleDayPickCallback { day ->
            if (day.year<calendar.year){
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT).show()
            }else if (day.month<calendar.month){
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT).show()

            }else if (day.dayOfMonth<calendar.dayOfMonth){
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT).show()
            }else{
                val month = (day.month+1)
                date = "${day.year} / $month /  ${day.date}"
                getDateFilter(date!!)
            }

        }



        val datePicker = PrimeDatePicker.dialogWith(calendar)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(calendar)
            .applyTheme(customCalender)
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")
    }


}