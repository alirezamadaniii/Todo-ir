package com.example.todoir.peresentaion.ui.addtask

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
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentAddTaskBinding
import com.example.todoir.peresentaion.adapter.CategoryAdapter
import com.example.todoir.peresentaion.adapter.PriorityAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val viewModel: MainActivityViewModel by viewModels()


    private var date: String? = null
    private var time: String? = null
    private var category: String? = "All"
    private var categoryColor: String? = "All"
    private var categoryIcon: Int? = 0
    private var flag: String? = "0"
    private var today: CivilCalendar? = null


    @Inject
    lateinit var sp: Sp
    private val customCalender = CustomCalender()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_task, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()

    }

    private fun onClick() {
        binding.imgCalender.setOnClickListener {
            chooseLangForCalender()
        }
        binding.imgCategory.setOnClickListener {
            selectCategory()
        }
        binding.imgPriority.setOnClickListener {
            selectPriority()
        }
        binding.imgAddTask.setOnClickListener {
            addTask()
        }
    }



    private fun chooseLangForCalender() {
            if (sp.fetch("language") == "Persian") {
                persianCalender()
            } else {
                englishCalender()
            }
    }
    private fun englishCalender() {
            val callback = SingleDayPickCallback { day ->
                val month = (day.month+1)
                date = "${day.year} / $month /  ${day.date}"
                Log.i("TAG", "persianCalender: " + day.year + "/" + month + "/" + day.date)
                timePiker()
            }

            today = CivilCalendar()


            val datePicker = PrimeDatePicker.bottomSheetWith(today!!)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(today!!)
                .applyTheme(customCalender)
                .build()

            datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")
        }

    private fun persianCalender() {
        today = CivilCalendar(TimeZone.getTimeZone("GMT+3:30"))

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
                    timePiker()
                }

            }



            val datePicker = PrimeDatePicker.bottomSheetWith(calendar)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(calendar)
                .applyTheme(customCalender)
                .build()

            datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")


        }

    private fun timePiker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        picker.show(requireActivity().supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            time = picker.hour.toString()+":"+picker.minute.toString()
            Toast.makeText(requireContext(),picker.hour.toString()+":"+picker.minute.toString(),Toast.LENGTH_SHORT).show()
        }
    }
    private fun selectCategory() {
            val category1 = Category(1,getString(R.string.add),R.drawable.add_1,"#80FFD1")
            val category2 = Category(2,getString(R.string.Grocery),R.drawable.bread_1,"#CCFF80")
            val category3 = Category(3,getString(R.string.Work),R.drawable.briefcase_1,"#FF9680")
            val category4 = Category(4,getString(R.string.Sport),R.drawable.sport_1,"#80FFFF")
            val category5 = Category(5,getString(R.string.Design),R.drawable.design__1__1,"#80FFD9")
            val category6 = Category(6,getString(R.string.University),R.drawable.mortarboard_1,"#809CFF")
            val category7 = Category(7,getString(R.string.Social),R.drawable.megaphone_1,"#FF80EB")
            val category8 = Category(8,getString(R.string.Music),R.drawable.music__1__1,"#FC80FF")
            val category9 = Category(9,getString(R.string.Health),R.drawable.heartbeat_1,"#80FFA3")
            val category10 = Category(10,getString(R.string.Movie),R.drawable.video_camera_1,"#FFCC80")
            val category11 = Category(11,getString(R.string.Home),R.drawable.home__2__1,"#80D1FF")

            val categoryList = arrayListOf(
                category1,category2,category3,
                category4,category5,category6,category7,
                category8,category9,category10,category11)
            val categoryDialog = requireActivity().dialog(R.layout.dialog_category,binding.root,true)
            val recycler =categoryDialog.findViewById<RecyclerView>(R.id.recy_category)
            val adapter = CategoryAdapter()
            adapter.differ.submitList(categoryList)
            recycler.adapter =adapter

            adapter.setOnItemClick {
                if (it.id==1){
                    categoryDialog.dismiss()
                    findNavController().navigate(R.id.action_addTaskFragment_to_createCategoryFragment)
                }else{
                    category= it.name
                    categoryColor = it.color
                    categoryIcon = it.image
                    categoryDialog.dismiss()
                }

            }
        }

    private fun selectPriority() {
                val priority = Priority(1,"1")
                val priority2 = Priority(2,"2")
                val priority3 = Priority(3,"3")
                val priority4 = Priority(4,"4")
                val priority5 = Priority(5,"5")
                val priority6 = Priority(6,"6")
                val priority7 = Priority(7,"7")
                val priority8 = Priority(8,"8")
                val priority9 = Priority(9,"9")
                val priority10 = Priority(10,"10")

                val priorityList = arrayListOf(
                    priority,
                    priority2,priority3,priority4,priority5,
                    priority6,priority7,priority8,priority9,priority10)
                val priorityDialog = requireActivity().dialog(R.layout.dialog_priority,binding.root,true)
                val recycler =priorityDialog.findViewById<RecyclerView>(R.id.recy_priority)
                val adapter = PriorityAdapter()
                adapter.differ.submitList(priorityList)
                recycler.adapter =adapter
                adapter.setOnItemClick {
                    flag = it.name
                    Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
                    priorityDialog.dismiss()
                }
         }

    private fun addTask() {

            val title = binding.edtTaskTitle.text.toString()
            val description = binding.edtTaskDescription.text.toString()
            val confirmTime = "${today?.hour}:${today?.minute}"
            val confirmDate = "${today?.year}/${today?.month}/${today?.dayOfMonth}"

            if(title.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.Please_enter_title), Toast.LENGTH_SHORT).show()
            }else if (description.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.Please_enter_Description), Toast.LENGTH_SHORT).show()

            }else{
                val task = Task(0,title,1,description,time.toString(),date.toString(),category.toString(),
                    categoryColor.toString(),
                    categoryIcon!!,
                    flag?.toInt()!!,confirmTime,confirmDate)
                viewModel.addTask(task)
                findNavController().navigate(R.id.homeFragment)
            }

    }





}