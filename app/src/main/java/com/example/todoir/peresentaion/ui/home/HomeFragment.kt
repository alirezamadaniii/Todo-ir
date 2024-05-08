package com.example.todoir.peresentaion.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.FragmentHomeBinding
import com.example.todoir.peresentaion.adapter.TaskAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private  val viewModel: MainActivityViewModel by viewModels()
    @Inject
    lateinit var adapter :TaskAdapter

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


    }

    private fun setImageProfile() {
        val imageUrl = sp.fetch("img_profile")
        Glide.with(requireContext()).load(imageUrl).into(binding.imProfileHome)
    }

    private fun showTask() {
        viewModel.getTask().observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                binding.consEmptyList.visibility = View.GONE
                binding.inItemHome.consHomeItem.visibility = View.VISIBLE
                adapter = TaskAdapter()
                binding.inItemHome.recyTask.adapter = adapter
                adapter.differ.submitList(it)


            }
        }

    }


}