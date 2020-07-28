package com.br.queroajudar.vacancydetails

import androidx.lifecycle.*
//import com.br.queroajudar.network.ApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.data.source.DefaultVacanciesRepository
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