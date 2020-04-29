package com.br.queroajudar.di

import com.br.queroajudar.view.fragments.VacanciesFragment
import dagger.Subcomponent

@Subcomponent
interface HomeComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }


    fun inject(fragment: VacanciesFragment)
}