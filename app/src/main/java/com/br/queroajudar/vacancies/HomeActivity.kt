package com.br.queroajudar.vacancies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        setupNav()
    }

    private fun setupNav(){
        navController = this.findNavController(R.id.activity_home_navhost)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.vacanciesFragment, R.id.favoritesFragment, R.id.profileFragment
        ))

        setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.activityHomeNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.vacanciesFragment -> setBottomNavVisibility(true)
                R.id.favoritesFragment -> setBottomNavVisibility(true)
                R.id.profileFragment -> setBottomNavVisibility(true)
                else -> setBottomNavVisibility(false)
            }
        }
    }

    private fun setBottomNavVisibility(visibility: Boolean){
        if(visibility){
            binding.activityHomeNavigationView.visibility = View.VISIBLE
        }
        else {
            binding.activityHomeNavigationView.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.activity_authentication_navhost)
        return navController.navigateUp()
    }
}
