package com.example.todoir.peresentaion.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.bumptech.glide.Glide
import com.example.todoir.R
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.SwipeToDelete
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentHomeBinding
import com.example.todoir.peresentaion.adapter.CategoryAdapter
import com.example.todoir.peresentaion.adapter.FilterBottomSheet
import com.example.todoir.peresentaion.adapter.PriorityAdapter
import com.example.todoir.peresentaion.adapter.TaskAdapter
import com.example.todoir.peresentaion.viewmodel.CreateCategoryViewModel
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val createCategoryViewModel: CreateCategoryViewModel by viewModels()

    @Inject
    lateinit var adapter: TaskAdapter

    private val itemAdapterBottomSheet = FilterBottomSheet()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var categoryAdapter: CategoryAdapter

    private var date: String? = null
    private var today: CivilCalendar? = null

    lateinit var data: List<Task>

    @Inject
    lateinit var sp: Sp
    private val customCalender = CustomCalender()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageProfile()
        showTask()
        onClick()
        getCategoryFromDb()
        filter()


    }

    private fun filter() {
        binding.filterTask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filterItems(p0.toString())
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterItems(query: String) {
        val filteredList = if (query.isEmpty()) {
            data // If query is empty, return the original list
        } else {
            data.filter { it.title.contains(query, ignoreCase = true) }
        }
        adapter.differ.submitList(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun onClick() {
        binding.imbFilter.setOnClickListener {
            showBottomSheet()
        }

        adapter.setOnItemClick {
            val bundle = Bundle().apply {
                putParcelable("task", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_updateFragment, bundle)
        }

        adapter.setOnCheckBoxClick { item, i ->
            if (i == 1) {
                viewModel.completedTask(item.taskId, true)
            } else {
                viewModel.completedTask(item.taskId, false)
            }
        }

    }

    private fun showBottomSheet() {
        val dialogView =
            layoutInflater.inflate(R.layout.bottom_sheet_filter, LinearLayout(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.setCancelable(true)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyyy_filter)
        recyclerView.adapter = itemAdapterBottomSheet
        bottomSheetDialog.show()

        bottomSheetItemClicked()
    }

    private fun bottomSheetItemClicked() {
        itemAdapterBottomSheet.setOnItemClick {
            when (it) {
                "All" ->{
                    binding.imgIsFilter.visibility = View.GONE
                    showTask()
                }
                "Done" -> {
                    binding.imgIsFilter.visibility = View.VISIBLE
                    getDoneFilter()
                }

                "Undone" -> {
                    binding.imgIsFilter.visibility = View.VISIBLE
                    getUnDoneFilter()
                }

                "Date" -> {
                    binding.imgIsFilter.visibility = View.VISIBLE
                    chooseLangForCalender()
                }

                "Priority" -> {
                    binding.imgIsFilter.visibility = View.VISIBLE
                    selectPriority()
                }

                "Category" -> {
                    binding.imgIsFilter.visibility = View.VISIBLE
                    selectCategory()

                }
            }
            bottomSheetDialog.dismiss()
        }
    }

    private fun getUnDoneFilter() {
        viewModel.getCompletedTask(false).observe(viewLifecycleOwner) {
            binding.consEmptyList.visibility = View.GONE
            binding.consHomeItem.visibility = View.VISIBLE
            binding.recyTask.adapter = adapter
            adapter.differ.submitList(it)
        }
    }

    private fun getDoneFilter() {
        viewModel.getCompletedTask(true).observe(viewLifecycleOwner) {
            binding.consEmptyList.visibility = View.GONE
            binding.consHomeItem.visibility = View.VISIBLE
            binding.recyTask.adapter = adapter
            adapter.differ.submitList(it)
        }
    }

    private fun selectPriority() {
        val priority = Priority(1, "1")
        val priority2 = Priority(2, "2")
        val priority3 = Priority(3, "3")
        val priority4 = Priority(4, "4")
        val priority5 = Priority(5, "5")


        val priorityList = arrayListOf(
            priority,
            priority2, priority3, priority4, priority5
        )
        val priorityDialog = requireActivity().dialog(R.layout.dialog_priority, binding.root, true)
        val recycler = priorityDialog.findViewById<RecyclerView>(R.id.recy_priority)
        val adapter = PriorityAdapter()
        adapter.differ.submitList(priorityList)
        recycler.adapter = adapter
        adapter.setOnItemClick {
            getPriorityFilter(it.name.toInt())
            priorityDialog.dismiss()
        }
    }

    private fun getPriorityFilter(priority: Int) {
        viewModel.getPriorityTask(priority).observe(viewLifecycleOwner) {
            binding.consEmptyList.visibility = View.GONE
            binding.consHomeItem.visibility = View.VISIBLE
            binding.recyTask.adapter = adapter
            adapter.differ.submitList(it)
        }
    }

    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            val month = (day.month + 1)
            date = "${day.year} / $month /  ${day.date}"
            getDateFilter(date.toString())
        }

        today = CivilCalendar()


        val datePicker = PrimeDatePicker.dialogWith(today!!)
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
            if (day.year < calendar.year) {
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            } else if (day.month < calendar.month) {
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT)
                    .show()

            } else if (day.dayOfMonth < calendar.dayOfMonth) {
                Toast.makeText(requireContext(), "لطفا تاریخ درست وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val month = (day.month + 1)
                date = "${day.year} / $month /  ${day.date}"
                getDateFilter(date.toString())
            }

        }


        val datePicker = PrimeDatePicker.dialogWith(calendar)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(calendar)
            .applyTheme(customCalender)
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "SOME_TAG")


    }

    private fun chooseLangForCalender() {
        if (sp.fetch("language") == "Persian") {
            persianCalender()
        } else {
            englishCalender()
        }
    }

    private fun getCategoryFromDb() {
        createCategoryViewModel.getCategory().observe(viewLifecycleOwner) {
            categoryAdapter = CategoryAdapter()
            categoryAdapter.differ.submitList(it.filter { item -> item.id != 1 })

        }
    }

    private fun selectCategory() {
        val categoryDialog = requireActivity().dialog(R.layout.dialog_category, binding.root, true)
        val recycler = categoryDialog.findViewById<RecyclerView>(R.id.recy_category)
        recycler.adapter = categoryAdapter

        categoryAdapter.setOnItemClick {
            getCategoryFilter(it.name)
            categoryDialog.dismiss()

        }
    }

    private fun getCategoryFilter(category: String) {
        viewModel.getCategoryTask(category).observe(viewLifecycleOwner) {
            binding.consEmptyList.visibility = View.GONE
            binding.consHomeItem.visibility = View.VISIBLE
            binding.recyTask.adapter = adapter
            adapter.differ.submitList(it)
        }
    }

    private fun getDateFilter(date: String) {
        viewModel.getTaskAfterFilter(date).observe(viewLifecycleOwner) {
            binding.consEmptyList.visibility = View.GONE
            binding.consHomeItem.visibility = View.VISIBLE
            binding.recyTask.adapter = adapter
            adapter.differ.submitList(it)
        }
    }

    private fun setImageProfile() {
        val imageUrl = sp.fetch("img_profile")
        Glide.with(requireContext()).load(imageUrl).into(binding.imProfileHome)
    }

    private fun showTask() {
        viewModel.getTask().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                data = it
                binding.consEmptyList.visibility = View.GONE
                binding.consHomeItem.visibility = View.VISIBLE
                binding.recyTask.adapter = adapter
                adapter.differ.submitList(it)
                swipeToDelete(binding.recyTask)
            } else {
                binding.consEmptyList.visibility = View.VISIBLE
                binding.consHomeItem.visibility = View.GONE
            }
        }

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.differ.currentList[viewHolder.adapterPosition]
                // Delete Item
                viewModel.deleteTask(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: Task) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            viewModel.addTask(deletedItem)
        }
        snackBar.show()
    }


}