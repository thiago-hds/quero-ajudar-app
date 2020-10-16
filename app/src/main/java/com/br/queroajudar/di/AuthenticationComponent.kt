package com.br.queroajudar.di

import com.br.queroajudar.categories.SelectCategoriesFragment
import com.br.queroajudar.login.LoginFragment
import com.br.queroajudar.register.RegisterFragment
import com.br.queroajudar.register.StartFragment
import dagger.Subcomponent

@Subcomponent
interface AuthenticationComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthenticationComponent
    }

    fun inject(fragment: StartFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SelectCategoriesFragment)
}