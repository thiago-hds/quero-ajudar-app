package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.Cause
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

    // Repositorios
    private val vacancyRepository = VacancyRepository()
    private val globalRepository = QueroAjudarRepository()

    // Dados observaveis
    private val _apiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val apiStatus: LiveData<QueroAjudarApiStatus>
        get() = _apiStatus

    private var _vacancies = MutableLiveData<MutableList<Vacancy>>()
    val vacancies : LiveData<MutableList<Vacancy>>
        get() = _vacancies

    // Variaveis de controle
    private var _page = 1
    private var _endLoading = false

    // Escopo de Corotina
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init{
        _vacancies.value = mutableListOf()
        loadVacancies()
    }


    /*
     *  Eventos da UI
     */

    fun onListScrollReachEnd(){
        if(_apiStatus != QueroAjudarApiStatus.LOADING && !_endLoading) {
            loadVacancies()
        }
    }

    fun onVacancyClicked(){
        //TODO
    }


    private fun loadVacancies(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading vacancies page $_page")
        _apiStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getVacanciesResponse = vacancyRepository.getVacancies(_page)) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getVacanciesResponse)
                is ResultWrapper.Success -> onLoadVacanciesSuccess(getVacanciesResponse.value)
            }
        }

    }

    private fun loadCauses(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading causes")
        _apiStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getCausesResponse = globalRepository.getCauses()) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getCausesResponse)
                is ResultWrapper.Success -> onLoadCausesSuccess(getCausesResponse.value)
            }
        }

    }

    private fun onLoadCausesSuccess(value: SuccessResponse<List<Cause>>) {
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

    private fun onLoadVacanciesSuccess(value: SuccessResponse<List<Vacancy>>) {
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