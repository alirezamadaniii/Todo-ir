package com.example.todoir.peresentaion.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todoir.R
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentProfileBinding
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private  val viewModel: MainActivityViewModel by viewModels()
    private lateinit var profileImage:ImageView
    private lateinit var dialog:Dialog
    private val mutableLiveData= MutableLiveData<String>()

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val itemAdapterBottomSheet = LanguageBottomSheet()

    private val _isLoading = MutableStateFlow(true)
    private lateinit var navController: NavController

    @Inject
    lateinit var sp: Sp

    private var imageUri: Uri?=null

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

        mutableLiveData.value = sp.fetch("username").toString()

        setName()
        showTask()
        onClick()



    }

    private fun onClick() {
        binding.constraintLayout.setOnClickListener {
            setUpAccountChangerDialog()
        }
        binding.consLogOut.setOnClickListener {
            sp.clear()
            findNavController().navigate(R.id.action_profileFragment_to_intro_navigation)
        }
    }


    private fun setLocal(langCode: String, direction: Int) {
        val local = Locale(langCode)
        val resource: Resources = resources
        val config: Configuration = resource.configuration
        config.setLocale(local)
        resource.updateConfiguration(config, resource.displayMetrics)
        ViewCompat.setLayoutDirection(binding.root, direction)
        refreshCurrentFragment()
    }

    private fun refreshCurrentFragment() {
        // Recreate the Fragment to apply language changes
        requireActivity().supportFragmentManager
            .beginTransaction()
            .detach(this)
            .attach(this)
            .commit();
    }


    private fun setUpAccountChangerDialog() {


        dialog = requireContext().dialog(R.layout.dialog_change_account,binding.root,true)
        profileImage=dialog.findViewById<ImageView>(R.id.im_profile_setting)
        val imageChose = dialog.findViewById<TextView>(R.id.im_choose_image_setting)
        val edtUsername = dialog.findViewById<EditText>(R.id.edt_name_setting)
        val btnSaveChanges = dialog.findViewById<Button>(R.id.btn_login_setting)

        if (imageUri!=null){
            Glide.with(binding.root)
                .load(sp.fetch("img_profile"))
                .into(profileImage)
        }


        btnSaveChanges.setOnClickListener {

            if (edtUsername.text.isNullOrEmpty()){
                Toast.makeText(requireContext(),"please enter name",Toast.LENGTH_SHORT).show()
            }else {
                if (imageUri==null){
                    mutableLiveData.value = edtUsername.text.toString()
                    sp.data("username", edtUsername.text.toString())
                    dialog.dismiss()
                }else{
                    mutableLiveData.value = edtUsername.text.toString()
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
                val lang :String = sp.fetch("language").toString()
                if (lang=="Persian") setLocal("fa",1) else setLocal("en",0)
                val intent = data.data
                imageUri = intent?.data!!
                Glide.with(requireContext()).load(imageUri).into(profileImage)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    private fun setName() {
        mutableLiveData.observe(viewLifecycleOwner){
            binding.tvUserNameProfile.text = it.toString()
        }

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