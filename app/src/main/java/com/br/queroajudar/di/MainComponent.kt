package com.br.queroajudar.di

import com.br.queroajudar.login.LoginFragment
import com.br.queroajudar.register.RegisterFragment
import com.br.queroajudar.register.StartFragment
import com.br.queroajudar.vacancies.VacanciesFragment
import com.br.queroajudar.vacancydetails.VacancyDetailsFragment
import dagger.Subcomponent

@Subcomponent
interface MainComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }


    fun inject(fragment: StartFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
}