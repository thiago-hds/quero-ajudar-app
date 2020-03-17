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
    private val _getVacanciesStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getVacanciesStatus: LiveData<QueroAjudarApiStatus>
        get() = _getVacanciesStatus

    private val _getCausesStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getCausesStatus: LiveData<QueroAjudarApiStatus>
        get() = _getCausesStatus

    private val _getSkillsStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getSkillsStatus: LiveData<QueroAjudarApiStatus>
        get() = _getSkillsStatus


    private var _vacancies = MutableLiveData<MutableList<Vacancy>>()
    val vacancies : LiveData<MutableList<Vacancy>>
        get() = _vacancies

    private var _causes = MutableLiveData<List<Cause>>()
    val causes : LiveData<List<Cause>>
        get() = _causes

    // Variaveis de controle
    private var _page = 1
    private var _endLoading = false

    // Escopo de Corotina
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init{
        _vacancies.value = mutableListOf()
        _causes.value = mutableListOf()
        loadVacancies()
    }


    /*
     *  Eventos da UI
     */

    fun onListScrollReachEnd(){
        if(_getVacanciesStatus != QueroAjudarApiStatus.LOADING && !_endLoading) {
            loadVacancies()
        }
    }

    fun onDrawerOpened(){
        _causes.value?.let{
            if(it.isEmpty()){
                loadCauses()
            }
        }
    }

    fun onVacancyClicked(){
        //TODO
    }


    private fun loadVacancies(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading vacancies page $_page")
        _getVacanciesStatus.value = QueroAjudarApiStatus.LOADING
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
        _getCausesStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getCausesResponse = globalRepository.getCauses()) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getCausesResponse)
                is ResultWrapper.Success -> onLoadCausesSuccess(getCausesResponse.value)
            }
        }

    }

    private fun onLoadCausesSuccess(value: SuccessResponse<List<Cause>>) {
        _getCausesStatus.value = QueroAjudarApiStatus.DONE
        Timber.tag("QueroAjudar").i("Causes API call success: $value")
        _causes.value = value.data
    }


    private fun onNetworkError( ){
        Timber.tag("QueroAjudar").i("API call network error")
        _getVacanciesStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
    }

    private fun onGenericError(loginResponse: ResultWrapper.GenericError) {
        Timber.tag("QueroAjudar").i("API call generic error: $loginResponse")
        _getVacanciesStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
    }

    private fun onLoadVacanciesSuccess(value: SuccessResponse<List<Vacancy>>) {
        Timber.tag("QueroAjudar").i("Vacancies API call success: $value")
        val newVacancies = value.data
        if(newVacancies?.isNotEmpty()!!){
            _vacancies.value?.addAll(newVacancies)
            _page ++
        }
        else{
            _endLoading = true
        }
        //_vacancies.value = value.data

        _getVacanciesStatus.value = QueroAjudarApiStatus.DONE

    }
}