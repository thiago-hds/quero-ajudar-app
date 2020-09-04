package com.br.queroajudar.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.br.queroajudar.QueroAjudarApplication
import com.br.queroajudar.R
import com.br.queroajudar.databinding.ActivityAuthenticationBinding
import com.br.queroajudar.di.AuthenticationComponent

class AuthenticationActivity : AppCompatActivity() {

    lateinit var authenticationComponent: AuthenticationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        authenticationComponent = (application as QueroAjudarApplication).appComponent.mainComponent().create()
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAuthenticationBinding>(this,
            R.layout.activity_authentication
        )
        val navController = this.findNavController(R.id.activity_authentication_navhost)
        NavigationUI.setupActionBarWithNavController(this, navController)
        supportActionBar?.hide()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.activity_authentication_navhost)
        return navController.navigateUp()
    }
}
