package com.br.queroajudar.vacancies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.br.queroajudar.QueroAjudarApplication
import com.br.queroajudar.R
import com.br.queroajudar.databinding.ActivityMainBinding
import com.br.queroajudar.di.MainComponent
import com.br.queroajudar.register.AuthenticationActivity

class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (application as QueroAjudarApplication)
            .appComponent.mainComponent().create()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
        val navController = this.findNavController(
            R.id.activity_authentication_navhost
        )
        return navController.navigateUp()
    }

    fun goToAuthenticationActivity(){
        var intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
}