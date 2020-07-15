package com.br.queroajudar.vacancydetails

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.br.queroajudar.data.Category
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.di.CoroutineScopeIO
//import com.br.queroajudar.network.ApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.util.VacancyPagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.properties.Delegates

class VacancyDetailsViewModel @Inject constructor(
    private val repository: DefaultVacanciesRepository
) : ViewModel(){

    var id by Delegates.notNull<Int>()

    val vacancy by lazy { repository.getVacancy(id) }

    fun favoriteVacancy(): LiveData<ResultWrapper<Boolean>> {
        return repository.favoriteVacancy(id)
    }
}