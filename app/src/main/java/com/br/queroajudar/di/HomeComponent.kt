package com.br.queroajudar.di

import com.br.queroajudar.vacancies.VacanciesFragment
import com.br.queroajudar.vacancydetails.VacancyDetailsFragment
import dagger.Subcomponent

@Subcomponent
interface HomeComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }


    fun inject(fragment: VacanciesFragment)
    fun inject(fragment: VacancyDetailsFragment)
}