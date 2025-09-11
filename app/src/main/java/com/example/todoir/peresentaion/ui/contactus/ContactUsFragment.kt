package com.example.todoir.peresentaion.ui.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.todoir.R
import com.example.todoir.databinding.FragmentContactUsBinding


class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =DataBindingUtil.inflate(layoutInflater,R.layout.fragment_contact_us, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtGmail.setOnClickListener {
            openGmail()
        }

        binding.txtId.setOnClickListener {
            openTelegram()
        }


    }

    private fun openTelegram() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/alirezaaamad"))
        startActivity(intent)
    }

    private fun openGmail() {

        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "FocusFlow1403@gmail.com", null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text")
        requireContext().startActivity(Intent.createChooser(emailIntent, null))
    }
}