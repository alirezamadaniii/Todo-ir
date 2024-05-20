package com.example.todoir.peresentaion.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.FragmentProfileBinding
import com.example.todoir.peresentaion.adapter.TaskAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private  val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var sp: Sp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setName()
        showTask()
    }

    private fun setName() {
        binding.tvUserNameProfile.text = sp.fetch("username").toString()
    }


    private fun showTask() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getTask().observe(viewLifecycleOwner){
                val doneList =it.map { it.isCompleted }
                var a = 0
                var b = 0
                doneList.forEach {
                    if (it){
                        a += 1
                    }else{
                        b += 1
                    }
                    binding.tvDoneTask.text ="$a Task Done"
                    binding.tvLeftTask.text ="$b Task Left"
                }

            }
        }


    }

}