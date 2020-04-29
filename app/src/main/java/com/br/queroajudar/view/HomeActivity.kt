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
import com.br.queroajudar.QueroAjudarApplication
import com.br.queroajudar.R
import com.br.queroajudar.databinding.ActivityHomeBinding
import com.br.queroajudar.di.HomeComponent
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    lateinit var homeComponent: HomeComponent

    private lateinit var binding : ActivityHomeBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        homeComponent = (application as QueroAjudarApplication).appComponent.homeComponent().create()
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = this.findNavController(R.id.activity_home_navhost)
        setupActionBarWithNavController(this, navController)
        binding.activityHomeNavigationView.setupWithNavController(navController)
    }
}
