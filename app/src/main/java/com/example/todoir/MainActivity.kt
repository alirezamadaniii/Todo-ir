package com.example.todoir

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
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
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.ActivityMainBinding
import com.example.todoir.peresentaion.adapter.CategoryAdapter
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.example.todoir.peresentaion.adapter.PriorityAdapter
import com.example.todoir.peresentaion.viewmodel.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
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

    private  val viewModel: MainActivityViewModel by viewModels()

    private var date:String? = null
    private var time:String? = null
    private var category:String? ="All"
    private var flag:String? = "0"
    private var today:CivilCalendar? = null


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
            addTaskShowBottomSheet()
        }


    }


    private fun timePiker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        val isSystem24Hour = is24HourFormat(this)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        picker.show(this.supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            time = picker.hour.toString()+":"+picker.minute.toString()
            Toast.makeText(this,picker.hour.toString()+":"+picker.minute.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun englishCalender() {
        val callback = SingleDayPickCallback { day ->
            val month = (day.month+1)
            date = "${day.year} / $month /  ${day.date}"
            Log.i("TAG", "persianCalender: " + day.year + "/" + month + "/" + day.date)
            timePiker()
        }

        today = CivilCalendar()


        val datePicker = PrimeDatePicker.bottomSheetWith(today!!)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today!!)
            .applyTheme(customCalender)
            .build()

        datePicker.show(supportFragmentManager, "SOME_TAG")
    }

    private fun persianCalender() {
        val calendar = PersianCalendar(TimeZone.getTimeZone("GMT+4:30"))

        val callback = SingleDayPickCallback { day ->
            val month = (day.month+1)
            date = "${day.year} / $month /  ${day.date}"
            Log.i("TAG", "persianCalender: " + day.year + "/" + month + "/" + day.date)
            timePiker()

        }

        today = CivilCalendar(TimeZone.getTimeZone("GMT+4:30"))

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
        navController.popBackStack(id, true)
        navController.navigate(id)
    }


    private fun addTaskShowBottomSheet() {
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
            val category1 = Category(1,getString(R.string.add),R.drawable.add_1,"#80FFD1")
            val category2 = Category(2,getString(R.string.Grocery),R.drawable.bread_1,"#CCFF80")
            val category3 = Category(3,getString(R.string.Work),R.drawable.briefcase_1,"#FF9680")
            val category4 = Category(4,getString(R.string.Sport),R.drawable.sport_1,"#80FFFF")
            val category5 = Category(5,getString(R.string.Design),R.drawable.design__1__1,"#80FFD9")
            val category6 = Category(6,getString(R.string.University),R.drawable.mortarboard_1,"#809CFF")
            val category7 = Category(7,getString(R.string.Social),R.drawable.megaphone_1,"#FF80EB")
            val category8 = Category(8,getString(R.string.Music),R.drawable.music__1__1,"#FC80FF")
            val category9 = Category(9,getString(R.string.Health),R.drawable.heartbeat_1,"#80FFA3")
            val category10 = Category(10,getString(R.string.Movie),R.drawable.video_camera_1,"#FFCC80")
            val category11 = Category(11,getString(R.string.Home),R.drawable.home__2__1,"#80D1FF")

            val categoryList = arrayListOf(
                category1,category2,category3,
                category4,category5,category6,category7,
                category8,category9,category10,category11)
            val categoryDialog = dialog(R.layout.dialog_category,binding.root,true)
            val recycler =categoryDialog.findViewById<RecyclerView>(R.id.recy_category)
            val adapter =CategoryAdapter()
            adapter.differ.submitList(categoryList)
            recycler.adapter =adapter

            adapter.setOnItemClick {
                category= it.name
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
                categoryDialog.dismiss()
            }
        }


        dialogView.findViewById<ImageView>(R.id.img_priority).setOnClickListener {
            val priority = Priority(1,"1")
            val priority2 = Priority(2,"2")
            val priority3 = Priority(3,"3")
            val priority4 = Priority(4,"4")
            val priority5 = Priority(5,"5")
            val priority6 = Priority(6,"6")
            val priority7 = Priority(7,"7")
            val priority8 = Priority(8,"8")
            val priority9 = Priority(9,"9")
            val priority10 = Priority(10,"10")

            val priorityList = arrayListOf(
                priority,
                priority2,priority3,priority4,priority5,
                priority6,priority7,priority8,priority9,priority10)
            val priorityDialog = dialog(R.layout.dialog_priority,binding.root,true)
            val recycler =priorityDialog.findViewById<RecyclerView>(R.id.recy_priority)
            val adapter =PriorityAdapter()
            adapter.differ.submitList(priorityList)
            recycler.adapter =adapter
            adapter.setOnItemClick {
                flag = it.name
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
                priorityDialog.dismiss()
            }
        }

        dialogView.findViewById<ImageView>(R.id.img_add_task).setOnClickListener {

            val title = dialogView.findViewById<EditText>(R.id.edt_task_title).text.toString()
            val description = dialogView.findViewById<EditText>(R.id.edt_task_description).text.toString()
            val confirmDate = "${today?.hour}  ${today?.minute}"
            Log.i("TAG", "addTaskShowBottomSheet: "+confirmDate)

            if(title.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.Please_enter_title), Toast.LENGTH_SHORT).show()
            }else if (description.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.Please_enter_Description), Toast.LENGTH_SHORT).show()

            }else{
                val task =Task(0,title,1,description,time.toString(),date.toString(),category.toString(),
                    flag?.toInt()!!,"12.23")
                viewModel.addTask(task)
                bottomSheetDialog2.dismiss()
            }


        }
    }





}