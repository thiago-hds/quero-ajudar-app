package com.br.queroajudar.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.queroajudar.viewmodel.VacanciesViewModel
import com.br.queroajudar.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VacanciesViewModel::class)
    internal abstract fun vacanciesViewModel(viewModel: VacanciesViewModel): ViewModel

    //Add more ViewModels here
}