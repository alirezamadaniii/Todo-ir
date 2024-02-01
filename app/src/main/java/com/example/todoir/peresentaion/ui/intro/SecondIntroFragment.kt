package com.example.todoir.peresentaion.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.todoir.R
import com.example.todoir.databinding.FragmentSecondIntroBinding


class SecondIntroFragment : Fragment() {
    private lateinit var binding: FragmentSecondIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_second_intro, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSecondFirstPage.setOnClickListener {
            findNavController().navigate(R.id.action_secondIntroFragment_to_registerFragment)
        }
        binding.btnBackSecondPage.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}