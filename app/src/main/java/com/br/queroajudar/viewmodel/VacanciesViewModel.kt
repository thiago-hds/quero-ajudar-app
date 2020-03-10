package com.br.queroajudar.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
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

    private var _vacancies = MutableLiveData<MutableList<Vacancy>>()
    val vacancies : LiveData<MutableList<Vacancy>>
        get() = _vacancies

    private var _page = 1
    private var _endLoading = false

    init{
        _vacancies.value = mutableListOf<Vacancy>()
        loadVacancies()
    }

    fun onListEndReached(){
        if(_apiStatus != QueroAjudarApiStatus.LOADING && !_endLoading) {
            loadVacancies()
        }
    }


    private fun loadVacancies(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading vacancies page $_page")
        _apiStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getVacanciesResponse = repository.getVacancies(_page)) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getVacanciesResponse)
                is ResultWrapper.Success -> onSuccess(getVacanciesResponse.value)
            }
        }

    }

    fun onVacancyClicked(){
        //TODO
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
        val newVacancies = value.data
        if(newVacancies?.isNotEmpty()!!){
            _vacancies.value?.addAll(newVacancies)
            _page ++
        }
        else{
            _endLoading = true
        }
        //_vacancies.value = value.data

        _apiStatus.value = QueroAjudarApiStatus.DONE

    }
}