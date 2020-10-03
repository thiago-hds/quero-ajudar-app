package com.br.queroajudar.vacancyapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.Vacancy
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


    lateinit var vacancy: Vacancy

    fun applyForVacancyOrCancelApplication(): LiveData<ResultWrapper<Boolean>> {
        vacancyApplicationData.vacancyId = vacancy.id

        return if(vacancy.application != null && vacancy.application?.status == 1){
            repository.cancelApplicationForVacancy(vacancy.application!!.id)
        } else {
            repository.applyForVacancy(vacancyApplicationData)
        }
    }
}