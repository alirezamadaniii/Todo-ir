package com.example.todoir

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.todoir.data.model.Category
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.ActivityMainBinding
import com.example.todoir.peresentaion.adapter.CategoryAdapter
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    @Inject
    lateinit var sp: Sp

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetDialog2: BottomSheetDialog
    private val itemAdapterBottomSheet = LanguageBottomSheet()
    private val customCalender = CustomCalender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        createCustomBottomNavigation()
        initNavigation()
        initBottomNavigation()
        hideBottomNavigation()

        binding.fab.setOnClickListener {
            addTaskshowBottomSheet()


        }


    }


    private fun timePiker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        val isSystem24Hour = is24HourFormat(this)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        picker.show(this.supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            Toast.makeText(this,picker.hour.toString()+":"+picker.minute.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            Log.i("TAG", "persianCalender: " + day.year + "/" + day.month + "/" + day.date)
            timePiker()
        }

        val today = CivilCalendar()

        val datePicker = PrimeDatePicker.bottomSheetWith(today)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today)
            .applyTheme(customCalender)
            .build()

        datePicker.show(supportFragmentManager, "SOME_TAG")
    }

    private fun persianCalender() {
        val calendar = PersianCalendar(TimeZone.getTimeZone("GMT+4:30"))

        val callback = SingleDayPickCallback { day ->
            Log.i("TAG", "persianCalender: " + day.year + "/" + day.month + "/" + day.date)
            timePiker()

        }

        val today = CivilCalendar(TimeZone.getTimeZone("GMT+4:30"))

        val datePicker = PrimeDatePicker.bottomSheetWith(calendar)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(calendar)
            .applyTheme(customCalender)
            .build()

        datePicker.show(supportFragmentManager, "SOME_TAG")


    }

    private fun createCustomBottomNavigation() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
    }

    // hide bottom navigation from pages not bottom nav uses
    private fun hideBottomNavigation() {
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
//                R.id.secondIntroFragment -> {
//                    binding.bottomAppBar.visibility = View.GONE
//                    binding.fab.visibility =View.GONE
//                }
//                R.id.registerFragment -> {
//                    binding.bottomAppBar.visibility = View.GONE
//                    binding.fab.visibility =View.GONE
//                }
//                R.id.welcomeIntroFragment -> {
//                    binding.bottomAppBar.visibility = View.GONE
//                    binding.fab.visibility =View.GONE
//                }
                else -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                }
            }
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //setup nav from home or first intro page
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.main_nav)
        val lang = sp.fetch("username").toString()
        val destination: Int = if (lang.isNullOrEmpty()) {
            showBottomSheet()
            R.id.intro_navigation
        } else {
            R.id.homeFragment

        }
        navGraph.setStartDestination(destination)
        navController.graph = navGraph

    }

    private fun initBottomNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)
    }


    private fun showBottomSheet() {
        val dialogView =
            layoutInflater.inflate(R.layout.bottom_sheet, LinearLayout(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.setCancelable(false)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyyy)
        recyclerView.adapter = itemAdapterBottomSheet
        bottomSheetDialog.show()

        bottomSheetItemClicked()
    }

    private fun bottomSheetItemClicked() {
        itemAdapterBottomSheet.setOnItemClick {
            chooseLang(it)
            sp.data("language", it)
            bottomSheetDialog.dismiss()
            _isLoading.value = false
        }
    }

    private fun chooseLang(it: String) {
        if (it == "Persian") {
            setLocal("fa", 1)
        } else {
            setLocal("en", 0)
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
        val id = R.id.intro_navigation
        navController.popBackStack(id!!, true)
        navController.navigate(id)
    }


    private fun addTaskshowBottomSheet() {
        val dialogView =
            layoutInflater.inflate(R.layout.add_task_bottom_sheet, LinearLayout(this))
        bottomSheetDialog2 =
            BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog2.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        bottomSheetDialog2.setContentView(dialogView)
        bottomSheetDialog2.setCancelable(true)
        bottomSheetDialog2.show()

        dialogView.findViewById<ImageView>(R.id.img_calender).setOnClickListener {
            if (sp.fetch("language") == "Persian") {
                persianCalender()
            } else {
                englishCalender()
            }
        }


        dialogView.findViewById<ImageView>(R.id.img_category).setOnClickListener {
            val category = Category(1,"Sport",R.drawable.sport_1)
            val category1 = Category(2,"Sport",R.drawable.sport_1)
            val category2 = Category(3,"Sport",R.drawable.sport_1)
            val category3 = Category(4,"Sport",R.drawable.sport_1)

            val category4 = Category(5,"Sport",R.drawable.sport_1)
            val category5 = Category(6,"Sport",R.drawable.sport_1)
            val category6 = Category(7,"Sport",R.drawable.sport_1)
            val category7 = Category(8,"Sport",R.drawable.sport_1)

            val categoryList = arrayListOf<Category>(category,category1,category2,category3,category4,category5,category6,category7)
            val categoryDialog = dialog(R.layout.dialog_category,binding.root,false)
            val recycler =categoryDialog.findViewById<RecyclerView>(R.id.recy_category)
            val adapter =CategoryAdapter()
            adapter.differ.submitList(categoryList)
            recycler.adapter =adapter
        }
    }


}