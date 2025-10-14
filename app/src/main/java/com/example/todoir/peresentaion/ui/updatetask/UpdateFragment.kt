package com.example.todoir.peresentaion.ui.updatetask

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.example.todoir.databinding.FragmentUpdateBinding
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
class UpdateFragment : Fragment() {

    private lateinit var binding:FragmentUpdateBinding
    private  val args:UpdateFragmentArgs by navArgs()

    @Inject
    lateinit var sp: Sp

    private var date: String? = null
    private var time: String? = null
    private var today: CivilCalendar? = null

    private var category: String? = "All"
    private var categoryColor: String? = "#80FFD1"
    private var categoryIcon: Int? = R.drawable.baseline_360_24

    private var flag: Int = 0

    private val customCalender = CustomCalender()

    private val createCategoryViewModel : CreateCategoryViewModel by viewModels()
    private  val viewModel: MainActivityViewModel by viewModels()
    private lateinit var adapter: CategoryAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var categoryDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_update, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sp.fetch("language").equals("fa")){
            category = "همه"
            date = "خالی"
            time = "خالی"
        }else{
            category = "All"
            date = "none"
            time = "none"
        }

        getTaskFromHome()
        getCategoryFromDb()
        onClick()
        editableText()
        toFillAfterCreteCategory()

    }

    private fun toFillAfterCreteCategory() {
        if (!args.title.equals("null")){
            binding.edtUpdateTitle.setText(args.title)
            binding.edtUpdateDescription.setText(args.description)
            time = args.time
            date = args.date
            binding.btTimeUpdate.text = time

            binding.btPriorityUpdate.text = args.task!!.flag.toString()


        }
    }


    private fun onClick() {
        binding.btTimeUpdate.setOnClickListener {
            chooseLangForCalender()
        }

        binding.btCategoryUpdate.setOnClickListener {
            selectCategory()
        }

        binding.btPriorityUpdate.setOnClickListener {
            selectPriority()
        }

        binding.consDeleteTask.setOnClickListener {
            removeTask()
        }

        binding.btnUpdateTask.setOnClickListener {
            updateTask()
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun editableText() {
        binding.apply {
            edtUpdateTitle.setScroller(Scroller(context))
            edtUpdateTitle.maxLines = 1
            edtUpdateTitle.isVerticalScrollBarEnabled = true
            edtUpdateTitle.movementMethod = ScrollingMovementMethod()
        }

    }

    private fun updateTask() {
        val confirmTime = "${today?.hour}:${today?.minute}"
        val confirmDate = "${today?.year}/${today?.month}/${today?.dayOfMonth}"
        val task =Task(
            args.task?.taskId!!,
            binding.edtUpdateTitle.text.toString(),
            1,
            binding.edtUpdateDescription.text.toString(),
            time.toString(),
            date.toString(),
            category.toString(),
            categoryColor.toString(),
            categoryIcon!!,
            flag,
            confirmTime,
            confirmDate,
            false
        )
        viewModel.updateTask(task)
        findNavController().popBackStack()
    }

    @SuppressLint("SetTextI18n")
    private fun getTaskFromHome() {
            binding.apply {
                edtUpdateTitle.setText(args.task?.title)
                edtUpdateDescription.setText(args.task?.description)
                btTimeUpdate.text = args.task?.time
                btCategoryUpdate.text = args.task?.categoryName
                btCategoryUpdate.icon = ContextCompat.getDrawable(requireContext(),
                    args.task?.categoryIcon!!
                )
                btPriorityUpdate.text = args.task!!.flag.toString()
                time = args.task!!.time
                date = args.task!!.date
                category = args.task!!.categoryName
                Log.i("TAG", "getTaskFromHome: $categoryColor")
                Log.i("TAG", "getTaskFromHome: ${args.task!!.flag}")
                categoryColor = args.task!!.categoryColor
                categoryIcon = args.task!!.categoryIcon
                flag = args.task!!.flag


        }

    }

    private fun chooseLangForCalender() {
        if (sp.fetch("language") == "fa") {
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
            binding.btTimeUpdate.text = time.toString()
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
        recycler =categoryDialog.findViewById(R.id.recy_category)
        recycler.adapter =adapter

        adapter.setOnItemClick {
            if (it.id==1){
                categoryDialog.dismiss()
                val title = args.task?.title.toString().ifEmpty{ "" }
                val description = args.task?.description.toString().ifEmpty { "" }
                val confirmTime = "${today?.hour}:${today?.minute}".ifEmpty { "" }
                val confirmDate = "${today?.year}/${today?.month}/${today?.dayOfMonth}".ifEmpty { "" }

                val bundle = Bundle().apply {
                    putString("title",title)
                    putString("description",description)
                    putString("time",confirmTime)
                    putString("date",confirmDate)
                }
                findNavController().navigate(R.id.action_updateFragment_to_createCategoryFragment,bundle)

            }else{
                category= it.name
                categoryColor = it.color
                categoryIcon = it.icon
                binding.btCategoryUpdate.text = it.name
                binding.btCategoryUpdate.icon = ContextCompat.getDrawable(requireContext(),it.icon)
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


        val priorityList = arrayListOf(
            priority,
            priority2,priority3,priority4,priority5
        )
        val priorityDialog = requireActivity().dialog(R.layout.dialog_priority,binding.root,true)
        val recycler =priorityDialog.findViewById<RecyclerView>(R.id.recy_priority)
        val adapter = PriorityAdapter()
        adapter.differ.submitList(priorityList)
        recycler.adapter =adapter
        adapter.setOnItemClick {
            flag = it.name.toInt()
            binding.btPriorityUpdate.text = it.name
            priorityDialog.dismiss()
        }
    }

    private fun removeTask() {
        viewModel.deleteTask(args.task!!)
        findNavController().popBackStack()
    }

}