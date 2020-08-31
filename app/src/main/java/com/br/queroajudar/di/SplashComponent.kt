package com.br.queroajudar.di

import com.br.queroajudar.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent
interface SplashComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    fun inject(activity: SplashActivity)
}