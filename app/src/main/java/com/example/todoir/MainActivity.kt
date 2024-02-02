package com.example.todoir

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.window.SplashScreen
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.LAYOUT_DIRECTION_RTL
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.data.utils.Sp
import com.example.todoir.databinding.ActivityMainBinding
import com.example.todoir.peresentaion.adapter.LanguageBottomSheet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    @Inject
    lateinit var sp:Sp

    private lateinit var  navController:NavController
    private lateinit var navGraph: NavGraph
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val itemAdapterBottomSheet = LanguageBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        createCustomBottomNavigation()
        initNavigation()
        initBottomNavigation()
        hideBottomNavigation()


    }

    private fun createCustomBottomNavigation(){
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
    }

    // hide bottom navigation from pages not bottom nav uses
    private fun hideBottomNavigation(){
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility =View.VISIBLE
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
                    binding.fab.visibility =View.GONE
                }
            }
        }
    }

    private fun initNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //setup nav from home or first intro page
        val graphInflater = navHostFragment.navController.navInflater
        navGraph = graphInflater.inflate(R.navigation.main_nav)
        val lang = sp.fetch("username").toString()
        val destination :Int= if (lang.isNullOrEmpty()) {
            showBottomSheet()
            R.id.firstIntroFragment
        }else{
            R.id.homeFragment

        }
        navGraph.setStartDestination(destination)
        navController.graph = navGraph

    }

    private fun initBottomNavigation(){
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

    private fun bottomSheetItemClicked(){
        itemAdapterBottomSheet.setOnItemClick {
            chooseLang(it)
            sp.data("language",it)
            bottomSheetDialog.dismiss()
            _isLoading.value = false
        }
    }

    private fun chooseLang(it: String) {
        if (it == "Persian"){
            setLocal("fa",1)
        }else{
            setLocal("en",0)
        }
    }

    private fun setLocal(langCode:String,direction:Int){
        val local=Locale(langCode)
        val resource :Resources = resources
        val config :Configuration = resource.configuration
        config.setLocale(local)
        resource.updateConfiguration(config,resource.displayMetrics)
        ViewCompat.setLayoutDirection(binding.root,direction)
        refreshCurrentFragment()
    }

    private fun refreshCurrentFragment(){
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }



}