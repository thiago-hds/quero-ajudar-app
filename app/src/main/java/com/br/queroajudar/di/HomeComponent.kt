package com.br.queroajudar.di

import com.br.queroajudar.categories.SelectCategoriesFragment
import com.br.queroajudar.vacancyapplication.VacancyApplicationFragment
import com.br.queroajudar.favorites.FavoriteOrganizationsFragment
import com.br.queroajudar.favorites.FavoriteVacanciesFragment
import com.br.queroajudar.organizationdetails.OrganizationDetailsFragment
import com.br.queroajudar.profile.ProfileFragment
import com.br.queroajudar.vacancies.VacanciesFragment
import com.br.queroajudar.vacancydetails.VacancyDetailsFragment
import dagger.Subcomponent

@Subcomponent
interface HomeComponent{

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }


    fun inject(fragment: SelectCategoriesFragment)
    fun inject(fragment: VacanciesFragment)
    fun inject(fragment: VacancyDetailsFragment)
    fun inject(fragment: VacancyApplicationFragment)
    fun inject(fragment: OrganizationDetailsFragment)
    fun inject(fragment: FavoriteVacanciesFragment)
    fun inject(fragment: FavoriteOrganizationsFragment)
    fun inject(fragment: ProfileFragment)
}