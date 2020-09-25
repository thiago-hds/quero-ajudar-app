package com.br.queroajudar.vacancyapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.data.formdata.VacancyApplicationData
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.network.ResultWrapper
import javax.inject.Inject
import kotlin.properties.Delegates

class VacancyApplicationViewModel @Inject constructor(
    private val repository: DefaultVacanciesRepository,
    val vacancyApplicationData: VacancyApplicationData
) : ViewModel(){

    var id by Delegates.notNull<Int>()

    val vacancy by lazy { repository.getVacancy(id) }

    fun applyForVacancy(): LiveData<ResultWrapper<Boolean>> {
        vacancyApplicationData.vacancyId = id
        return repository.applyForVacancy(vacancyApplicationData)
    }
}