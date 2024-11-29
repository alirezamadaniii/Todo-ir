package com.example.todoir.peresentaion.ui.setting

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentSettingBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import javax.inject.Inject

class SettingFragment : Fragment() {


    private lateinit var binding: FragmentSettingBinding

    @Inject
    lateinit var sp: Sp

    private var imageUri: Uri? = null

    private lateinit var profileImage: ImageView
    private lateinit var dialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }




    private fun onClick() {
        binding.constraintLayout.setOnClickListener {
            setUpAccountChangerDialog()
        }
        binding.consLogOut.setOnClickListener {
            val dialog = requireContext().dialog(R.layout.dialog_exit_app,binding.root,true)
            dialog.findViewById<Button>(R.id.btn_exit_dialog).setOnClickListener {
                sp.clear()
                findNavController().navigate(R.id.action_profileFragment_to_intro_navigation)
            }
            dialog.findViewById<Button>(R.id.btn_cancel_dialog).setOnClickListener {
                dialog.dismiss()
            }

        }
    }

    private fun setUpAccountChangerDialog() {


        dialog = requireContext().dialog(R.layout.dialog_change_account, binding.root, true)
        profileImage = dialog.findViewById<ImageView>(R.id.im_profile_setting)
        val imageChose = dialog.findViewById<TextView>(R.id.im_choose_image_setting)
        val edtUsername = dialog.findViewById<EditText>(R.id.edt_name_setting)
        val btnSaveChanges = dialog.findViewById<Button>(R.id.btn_login_setting)

        if (imageUri != null) {
            Glide.with(binding.root)
                .load(sp.fetch("img_profile"))
                .into(profileImage)
        }


        btnSaveChanges.setOnClickListener {

            if (edtUsername.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "please enter name", Toast.LENGTH_SHORT).show()
            } else {
                if (imageUri == null) {
                    sp.data("username", edtUsername.text.toString())
                    dialog.dismiss()
                } else {
                    sp.data("username", edtUsername.text.toString())
                    sp.data("img_profile", imageUri.toString())
                    dialog.dismiss()
                }

            }

        }

        imageChose.setOnClickListener {
            checkPermission()
        }

    }

    //check permission for get image with dexter library
    private fun checkPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(Intent.createChooser(intent, "open gallery"))
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    //get data from gallery intent
    var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { data ->
        if (data.resultCode == Activity.RESULT_OK) {
            try {
                val intent = data.data
                imageUri = intent?.data!!
                Glide.with(requireContext()).load(imageUri).into(profileImage)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }






}