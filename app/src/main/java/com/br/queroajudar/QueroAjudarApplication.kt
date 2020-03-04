package com.br.queroajudar

import android.app.Application
import com.br.queroajudar.util.QueroAjudarPreferences
import timber.log.Timber

class QueroAjudarApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        QueroAjudarPreferences.init(this)
    }
}