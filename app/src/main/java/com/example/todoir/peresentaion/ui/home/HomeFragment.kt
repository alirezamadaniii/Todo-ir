package com.example.todoir.peresentaion.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.SwipeToDelete
import com.example.todoir.databinding.FragmentHomeBinding
import com.example.todoir.peresentaion.adapter.TaskAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.todoir.data.model.Task
import com.example.todoir.peresentaion.adapter.FilterBottomSheet
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject




@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private  val viewModel: MainActivityViewModel by viewModels()
    @Inject
    lateinit var adapter :TaskAdapter

    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Task>
    private lateinit var dataList: ArrayList<Task>
    private val itemAdapterBottomSheet = FilterBottomSheet()
    private lateinit var bottomSheetDialog: BottomSheetDialog


    @Inject
    lateinit var sp: Sp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageProfile()
        showTask()
        onClick()




        searchList = arrayListOf<Task>()
        dataList = arrayListOf<Task>()
        adapter.setOnItemClick {
            val bundle = Bundle().apply {
                putParcelable("task",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_updateFragment,bundle)
        }

        adapter.setOnCheckBoxClick { item, i ->
            if (i==1) {
                viewModel.completedTask(item.taskId,true)
            }else {
                viewModel.completedTask(item.taskId,false)
            }
        }
    }

    private fun onClick() {
        binding.imbFilter.setOnClickListener {
            showBottomSheet()
        }

    }


    private fun showBottomSheet() {
        val dialogView =
            layoutInflater.inflate(R.layout.bottom_sheet_filter, LinearLayout(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.setCancelable(false)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyyy_filter)
        recyclerView.adapter = itemAdapterBottomSheet
        bottomSheetDialog.show()

        bottomSheetItemClicked()
    }

    private fun bottomSheetItemClicked() {
        itemAdapterBottomSheet.setOnItemClick {
            bottomSheetDialog.dismiss()
        }
    }


    private fun setImageProfile() {
        val imageUrl = sp.fetch("img_profile")
        Glide.with(requireContext()).load(imageUrl).into(binding.imProfileHome)
    }

    private fun showTask() {
        viewModel.getTask().observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                searchList.addAll(it)
                binding.consEmptyList.visibility = View.GONE
                binding.consHomeItem.visibility = View.GONE
                binding.consHomeItem.visibility = View.VISIBLE
                binding.recyTask.adapter = adapter
                adapter.differ.submitList(it)

//                binding.search.clearFocus()
//                binding.search.setOnQueryTextListener(object : Sear3chView.OnQueryTextListener{
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        binding.search.clearFocus()
//                        return true
//                    }
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        searchList .clear()
//                        val searchText = newText!!.toLowerCase(Locale.getDefault())
//                        if (searchText.isNotEmpty()){
//                            dataList.forEach{
//                                if (it.title.toLowerCase(Locale.getDefault()).contains(searchText)) {
//                                    searchList.add(it)
//                                }
//                            }
//                            binding.recyTask.adapter!!.notifyDataSetChanged()
//                        } else {
//                            searchList.clear()
//                            searchList.addAll(dataList)
//                            binding.recyTask.adapter!!.notifyDataSetChanged()
//                        }
//                        return false
//                    }
//                })
                swipeToDelete(binding.recyTask)
            }else{
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