package com.br.queroajudar.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.VacancyPagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.properties.Delegates

class FavoriteVacanciesViewModel @Inject constructor(
    private val vacanciesRepository: DefaultVacanciesRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){

    lateinit var pagedVacancies: VacancyPagedListing
    lateinit var vacancies: LiveData<PagedList<Vacancy>>
    lateinit var vacanciesLoadInitialResultWrapper: LiveData<ResultWrapper<Any>>
    lateinit var vacanciesLoadAfterResultWrapper: LiveData<ResultWrapper<Any>>

    init{
        loadVacancies()
    }

    fun loadVacancies() {
        pagedVacancies = vacanciesRepository.getPagedVacancies(coroutineScope, null)
        vacancies = pagedVacancies.pagedList
        vacanciesLoadInitialResultWrapper = pagedVacancies.loadInitialResultWrapper
        vacanciesLoadAfterResultWrapper = pagedVacancies.loadAfterResultWrapper
        //vacanciesSize = pagedVacancies.size
    }

}