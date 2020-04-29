package com.br.queroajudar.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AppSubcomponents::class])
interface AppComponent {
//    @Component.Factory
//    interface Factory{
//        fun create() : AppComponent
//    }

    // Expose HomeComponent factory from the graph
    fun homeComponent(): HomeComponent.Factory
}