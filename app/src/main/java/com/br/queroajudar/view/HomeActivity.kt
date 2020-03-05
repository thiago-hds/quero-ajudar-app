package com.br.queroajudar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.br.queroajudar.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViews()
    }

    fun setupViews()
    {
       // var navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_home_navhost) as NavHostFragment
        val navController = findNavController(R.id.activity_home_navhost)
        findViewById<BottomNavigationView>(R.id.activity_home_navigationView).setupWithNavController(navController)
        //navController = navHostFragment.navController
        //NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        //var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        //var appBarConfiguration = AppBarConfiguration(setOf(R.id.vacanciesFragment, R.id.favoritesFragment, R.id.profileFragment))
        //setupActionBarWithNavController(navController, appBarConfiguration)
    }


}
