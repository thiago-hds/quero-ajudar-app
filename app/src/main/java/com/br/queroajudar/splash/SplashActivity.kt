package com.br.queroajudar.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.queroajudar.QueroAjudarApplication
import com.br.queroajudar.di.SplashComponent
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.profile.ProfileViewModel
import com.br.queroajudar.register.AthenticationActivity
import com.br.queroajudar.vacancies.HomeActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    lateinit var splashComponent: SplashComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        splashComponent =
            (application as QueroAjudarApplication).appComponent.splashComponent().create()
        splashComponent.inject(this)
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        setupObservers()
    }

    private fun setupObservers(){
        viewModel.profile?.observe(this, Observer { result ->
            if(result is ResultWrapper.Success) {
                goToHomeActivity()
            }
            else if (result !is ResultWrapper.Loading){
                goToAuthenticationActivity()
            }
        })
    }

    private fun goToHomeActivity(){
        var intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }

    private fun goToAuthenticationActivity(){
        var intent = Intent(this, AthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        this.finish()
    }
}