package com.example.todoir

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.example.todoir.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var  navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        initBottomNavigation()


    }

    private fun initBottomNavigation(){

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)




        // hide bottom navigation
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.firstIntroFragment -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility =View.GONE
                }
                R.id.secondIntroFragment -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility =View.GONE
                }
                R.id.registerFragment -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility =View.GONE
                }
                R.id.welcomeIntroFragment -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility =View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.fab.visibility =View.VISIBLE
                }
            }
        }
    }
}