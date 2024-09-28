package com.example.todoir.peresentaion.ui.addtask

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.R
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentAddTaskBinding
import com.example.todoir.peresentaion.adapter.CategoryAdapter
import com.example.todoir.peresentaion.adapter.PriorityAdapter
import com.example.todoir.peresentaion.viewmodel.CreateCategoryViewModel
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
    private val createCategoryViewModel :CreateCategoryViewModel by viewModels()
    private lateinit var categoryDialog:Dialog
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val args: AddTaskFragmentArgs by navArgs()


    private var date: String? = null
    private var time: String? = null
    private var category: String? = null
    private var categoryColor: String? = null
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

        if (!args.title.equals("null")){
            binding.edtAddTitle.setText(args.title)
            binding.edtAddDescription.setText(args.description)
            time = args.time
            date = args.date
            binding.btTimeAdd.text = time


        }

        getCategoryFromDb()
        onClick()




    }



    private fun onClick() {
        binding.btTimeAdd.setOnClickListener {
            chooseLangForCalender()
        }
        binding.btCategoryAdd.setOnClickListener {
            selectCategory()
        }
        binding.btPriorityAdd.setOnClickListener {
            selectPriority()
        }
        binding.btnAddTask.setOnClickListener {
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
            binding.btTimeAdd.text = time.toString()
        }
    }


    private fun getCategoryFromDb() {
        createCategoryViewModel.getCategory().observe(viewLifecycleOwner){
            adapter = CategoryAdapter()
            adapter.differ.submitList(it)

        }
    }

    private fun selectCategory() {
        categoryDialog = requireActivity().dialog(R.layout.dialog_category,binding.root,true)
        recycler =categoryDialog.findViewById<RecyclerView>(R.id.recy_category)
        recycler.adapter =adapter
        
            adapter.setOnItemClick {
                if (it.id==1){
                    categoryDialog.dismiss()
                    val title = binding.edtAddTitle.text.toString().ifEmpty{ "" }
                    val description = binding.edtAddDescription.text.toString().ifEmpty { "" }
                    val confirmTime = "${today?.hour}:${today?.minute}".ifEmpty { "" }
                    val confirmDate = "${today?.year}/${today?.month}/${today?.dayOfMonth}".ifEmpty { "" }

                    val bundle = Bundle().apply {
                        putString("title",title)
                        putString("description",description)
                        putString("time",confirmTime)
                        putString("date",confirmDate)
                    }
                    findNavController().navigate(R.id.action_addTaskFragment_to_createCategoryFragment,bundle)

                }else{
                        category= it.name
                        categoryColor = it.color
                        categoryIcon = it.icon
                        binding.btCategoryAdd.text = it.name
                        binding.btCategoryAdd.icon = ContextCompat.getDrawable(requireContext(),it.icon)
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
                    binding.btPriorityAdd.text = it.name
                    priorityDialog.dismiss()
                }
         }

    private fun addTask() {

            val title = binding.edtAddTitle.text.toString()
            val description = binding.edtAddDescription.text.toString()
            val confirmTime = "${today?.hour}:${today?.minute}"
            val confirmDate = "${today?.year}/${today?.month}/${today?.dayOfMonth}"

            if(title.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.Please_enter_title), Toast.LENGTH_SHORT).show()
            }else if (description.isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.Please_enter_Description), Toast.LENGTH_SHORT).show()

            }else if (time.isNullOrEmpty()){
                time = "None"
            }else if (date.isNullOrEmpty()){
                date = "None"
            }else if (category.isNullOrEmpty()){
                category = "All"
                categoryColor = "#80FFD1"
                categoryIcon = R.drawable.add_1
            }else if (flag.isNullOrEmpty()){
                flag = "0"
            }else{
                val task = Task(0,title,1,description,time.toString(),date.toString(),category.toString(),
                    categoryColor.toString(),
                    categoryIcon!!,
                    flag?.toInt()!!,confirmTime,confirmDate,false)
                viewModel.addTask(task)
                findNavController().navigate(R.id.homeFragment)
            }

    }


    override fun onResume() {
        super.onResume()
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
            findNavController().navigate(R.id.action_addTaskFragment_to_homeFragment)
            return@OnKeyListener keyCode == KeyEvent.KEYCODE_BACK
        })
    }



}