package com.br.queroajudar.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.queroajudar.vacancies.VacanciesViewModel
import com.br.queroajudar.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

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