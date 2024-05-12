package com.example.todoir

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
import com.example.todoir.data.model.Category
import com.example.todoir.data.utils.CustomCalender
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.ActivityMainBinding
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.example.todoir.peresentaion.viewmodel.CreateCategoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val _isLoading = MutableStateFlow(true)
    private  val viewModel: CreateCategoryViewModel by viewModels()



    private var date:String? = null
    private var time:String? = null
    private var category:String? ="All"
    private var categoryColor:String? ="All"
    private var categoryIcon:Int? =0
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
        onClick()

    }

    private fun onClick() {
        binding.fab.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.navController

            //setup nav from home or first intro page
            val graphInflater = navHostFragment.navController.navInflater
            navGraph = graphInflater.inflate(R.navigation.main_nav)
            val destination: Int = R.id.addTaskFragment
            navGraph.setStartDestination(destination)
            navController.graph = navGraph
        }
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

                R.id.addTaskFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility =View.VISIBLE
                }
                R.id.profileFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility =View.VISIBLE
                }
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
        val destination: Int = if (lang.isEmpty()) {
            showBottomSheet()
            addPrimaryCategory()
            R.id.intro_navigation
        } else {
            chooseLang(sp.fetch("language").toString())
            R.id.homeFragment


        }
        navGraph.setStartDestination(destination)
        navController.graph = navGraph

    }

    private fun addPrimaryCategory() {

        viewModel.addCategory(Category(1,getString(R.string.add),R.drawable.add_1,"#80FFD1"))
        viewModel.addCategory(Category(2,getString(R.string.Grocery),R.drawable.bread_1,"#CCFF80"))
        viewModel.addCategory(Category(3,getString(R.string.Work),R.drawable.briefcase_1,"#FF9680"))
        viewModel.addCategory(Category(4,getString(R.string.Sport),R.drawable.sport_1,"#80FFFF"))
        viewModel.addCategory(Category(5,getString(R.string.Design),R.drawable.design__1__1,"#80FFD9"))
        viewModel.addCategory(Category(6,getString(R.string.University),R.drawable.mortarboard_1,"#809CFF"))
        viewModel.addCategory(Category(7,getString(R.string.Social),R.drawable.megaphone_1,"#FF80EB"))
        viewModel.addCategory(Category(8,getString(R.string.Music),R.drawable.music__1__1,"#FC80FF"))
        viewModel.addCategory(Category(9,getString(R.string.Health),R.drawable.heartbeat_1,"#80FFA3"))
        viewModel.addCategory(Category(10,getString(R.string.Movie),R.drawable.video_camera_1,"#FFCC80"))
        viewModel.addCategory(Category(11,getString(R.string.Home),R.drawable.home__2__1,"#80D1FF"))
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
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







}