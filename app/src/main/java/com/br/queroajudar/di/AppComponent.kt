package com.br.queroajudar.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppSubcomponents::class])
interface AppComponent {

    fun splashComponent(): SplashComponent.Factory
    fun authenticationComponent(): AuthenticationComponent.Factory
    fun mainComponent(): MainComponent.Factory
}