package com.br.queroajudar.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.br.queroajudar.categories.SelectCategoriesViewModel
import com.br.queroajudar.favorites.FavoriteOrganizationsViewModel
import com.br.queroajudar.favorites.FavoriteVacanciesViewModel
import com.br.queroajudar.login.LoginViewModel
import com.br.queroajudar.organizationdetails.OrganizationDetailsViewModel
import com.br.queroajudar.profile.ProfileViewModel
import com.br.queroajudar.register.RegisterViewModel
import com.br.queroajudar.vacancies.VacanciesViewModel
import com.br.queroajudar.util.ViewModelFactory
import com.br.queroajudar.vacancyapplication.VacancyApplicationViewModel
import com.br.queroajudar.vacancydetails.VacancyDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun registerViewModel(
        viewModel: RegisterViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(
        viewModel: LoginViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectCategoriesViewModel::class)
    internal abstract fun selectCategoriesViewModel(
        viewModel: SelectCategoriesViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VacanciesViewModel::class)
    internal abstract fun vacanciesViewModel(
        viewModel: VacanciesViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VacancyDetailsViewModel::class)
    internal abstract fun vacancyDetailsViewModel(
        viewModel: VacancyDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VacancyApplicationViewModel::class)
    internal abstract fun vacancyApplicationViewModel(
        viewModel: VacancyApplicationViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationDetailsViewModel::class)
    internal abstract fun organizationDetailsViewModel(
        viewModel: OrganizationDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteVacanciesViewModel::class)
    internal abstract fun favoriteVacanciesViewModel(
        viewModel: FavoriteVacanciesViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteOrganizationsViewModel::class)
    internal abstract fun favoriteOrganizationsViewModel(
        viewModel: FavoriteOrganizationsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(
        viewModel: ProfileViewModel
    ): ViewModel
}