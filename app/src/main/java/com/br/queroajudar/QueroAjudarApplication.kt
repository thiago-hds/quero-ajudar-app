package com.br.queroajudar

import android.app.Application
import com.br.queroajudar.di.AppComponent
import com.br.queroajudar.di.DaggerAppComponent
import com.br.queroajudar.util.QueroAjudarPreferences
import timber.log.Timber

/**
 * Principal componente da aplicação
 */

class QueroAjudarApplication : Application(){

    val appComponent: AppComponent = DaggerAppComponent.create()



    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        QueroAjudarPreferences.init(this)
    }
}