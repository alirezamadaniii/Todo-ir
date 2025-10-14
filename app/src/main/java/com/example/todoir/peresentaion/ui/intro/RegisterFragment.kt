package com.example.todoir.peresentaion.ui.intro

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.todoir.MainActivity
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.FragmentRegisterBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    @Inject
    lateinit var sp:Sp
    private var imageUri:Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            if (binding.edtNameRegister.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"please enter name",Toast.LENGTH_SHORT).show()
            }else{
                sp.data("img_profile",imageUri.toString())
                sp.data("username",binding.edtNameRegister.text.toString())
                findNavController().navigate(R.id.action_registerFragment_to_welcomeIntroFragment)
            }
        }
        binding.btnBackRegister.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imChooseImage.setOnClickListener {
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
                val lang :String = sp.fetch("language").toString()
                if (lang=="fa"){
                    ViewCompat.setLayoutDirection(binding.root, ViewCompat.LAYOUT_DIRECTION_RTL)
                    setLocal("fa",1)
                } else {
                    ViewCompat.setLayoutDirection(binding.root, ViewCompat.LAYOUT_DIRECTION_LTR)
                    setLocal("en",0)
                }
                val intent = data.data
                imageUri = intent?.data!!
                Glide.with(requireContext()).load(imageUri).into(binding.imProfileRegister)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setLocal(langCode:String,direction:Int){
        val local= Locale(langCode)
        val resource : Resources = resources
        val config : Configuration = resource.configuration
        config.setLocale(local)
        resource.updateConfiguration(config,resource.displayMetrics)
        ViewCompat.setLayoutDirection(binding.root,direction)
    }


}