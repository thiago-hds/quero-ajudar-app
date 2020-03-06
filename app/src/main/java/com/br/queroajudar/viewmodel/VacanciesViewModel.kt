package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.QueroAjudarRepository
import com.br.queroajudar.repository.VacancyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class VacanciesViewModel : ViewModel(){
    private var viewModelJob = Job()
    private val repository = VacancyRepository()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _apiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val apiStatus: LiveData<QueroAjudarApiStatus>
        get() = _apiStatus

    private var _vacancies = MutableLiveData<List<Vacancy>>()
    val vacancies : LiveData<List<Vacancy>>
        get() = _vacancies

    init{
        getVacancies()
    }

    private fun getVacancies(){
        _apiStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getVacanciesResponse = repository.getVacancies()) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getVacanciesResponse)
                is ResultWrapper.Success -> onSuccess(getVacanciesResponse.value)
            }
        }
    }

    private fun onNetworkError( ){
        Timber.tag("QueroAjudar").i("API call network error")
        _apiStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
    }

    private fun onGenericError(loginResponse: ResultWrapper.GenericError) {
        Timber.tag("QueroAjudar").i("API call generic error: $loginResponse")
        _apiStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
    }

    private fun onSuccess(value: SuccessResponse<List<Vacancy>>) {
        Timber.tag("QueroAjudar").i("API call success: $value")
        _vacancies.value = value.data
        _apiStatus.value = QueroAjudarApiStatus.DONE
    }
}