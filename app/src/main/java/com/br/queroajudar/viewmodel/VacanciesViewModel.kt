package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.Cause
import com.br.queroajudar.model.Skill
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.QueroAjudarRepository
import com.br.queroajudar.repository.VacancyRepository
import com.br.queroajudar.util.append
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

    private val _getFiltersStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getFiltersStatus: LiveData<QueroAjudarApiStatus>
        get() = _getFiltersStatus

    private var _vacancies = MutableLiveData<MutableList<Vacancy>>()
    val vacancies : LiveData<MutableList<Vacancy>>
        get() = _vacancies

    private var _causes = MutableLiveData<MutableList<Cause>>()
    val causes : LiveData<MutableList<Cause>>
        get() = _causes

    private var _skills = MutableLiveData<List<Skill>>()
    val skills : LiveData<List<Skill>>
        get() = _skills

    // Variaveis de controle
    private var _page = 1
    private var _selectedCauses = listOf<Int>()
    private var _selectedSkills = listOf<Int>()
    private var _endLoading = false
    private var _getCausesStatus = QueroAjudarApiStatus.DONE
    private var _getSkillsStatus = QueroAjudarApiStatus.DONE

    // Escopo de Corotina
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init{
        _vacancies.value = mutableListOf()
        _causes.value = mutableListOf()
        _skills.value = mutableListOf()

        _getVacanciesStatus.value   = QueroAjudarApiStatus.LOADING
        _getFiltersStatus.value     = QueroAjudarApiStatus.LOADING

        loadVacancies()
    }


    /*
     *  Eventos da UI
     */

    fun onListScrollReachEnd(){
        if(_getVacanciesStatus.value != QueroAjudarApiStatus.LOADING && !_endLoading) {
            loadVacancies()
        }
    }

    fun onVacancyClicked(){
        //TODO
    }

    fun onDrawerOpened(){
        // Carregar filtros
        if(causes.value.isNullOrEmpty() || skills.value.isNullOrEmpty()){
            loadCauses()
            loadSkills()
        }
    }

    fun onCauseItemSelected(selectedItems : List<Int>) {
        Timber.tag("QA.VacanciesViewModel").i("cause selected ${selectedItems.toString()}")
        _page = 1
        _selectedCauses = selectedItems
        _vacancies.value?.clear()
        loadVacancies()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.tag("QA.VacanciesViewModel").i("skill selected $selectedItems")
        _page = 1
        _selectedCauses = selectedItems
        _vacancies.value?.clear()
        loadVacancies()
    }


    private fun loadVacancies(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading vacancies page $_page")
        val strCauses = _selectedCauses.joinToString()
        val strSkills = _selectedSkills.joinToString ()

        _getVacanciesStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val getVacanciesResponse =
                    vacancyRepository.getVacancies(_page, strCauses, strSkills)) {
                is ResultWrapper.Success -> onLoadVacanciesSuccess(getVacanciesResponse.value)
                is ResultWrapper.NetworkError   ->
                    _getVacanciesStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
                is ResultWrapper.GenericError   ->
                    _getVacanciesStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
            }
        }
    }

    private fun loadCauses(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading causes")
        _getCausesStatus = QueroAjudarApiStatus.LOADING
        updateFiltersStatus()
        coroutineScope.launch {
            when (val getCausesResponse = globalRepository.getCauses()) {
                is ResultWrapper.Success        -> onLoadCausesSuccess(getCausesResponse.value)
                is ResultWrapper.NetworkError   -> _getCausesStatus = QueroAjudarApiStatus.NETWORK_ERROR
                is ResultWrapper.GenericError   -> _getCausesStatus = QueroAjudarApiStatus.GENERIC_ERROR
            }
        }
    }

    private fun loadSkills(){
        Timber.tag("QueroAjudar.VacanciesVM").i("Loading skills")
        _getSkillsStatus = QueroAjudarApiStatus.LOADING
        updateFiltersStatus()
        coroutineScope.launch {
            when (val getSkillsResponse = globalRepository.getSkills()) {
                is ResultWrapper.Success        -> onLoadSkillsSuccess(getSkillsResponse.value)
                is ResultWrapper.NetworkError   -> _getSkillsStatus = QueroAjudarApiStatus.NETWORK_ERROR
                is ResultWrapper.GenericError   -> _getSkillsStatus = QueroAjudarApiStatus.GENERIC_ERROR
            }
        }
    }

    private fun onLoadVacanciesSuccess(value: SuccessResponse<List<Vacancy>>) {
        Timber.tag("QueroAjudar").i("Vacancies API call success: $value")
        val newVacancies = value.data
        if(newVacancies?.isNotEmpty()!!){
            _vacancies.append(newVacancies)
            _page ++
        }
        else{
            _endLoading = true
        }
        _getVacanciesStatus.value = QueroAjudarApiStatus.DONE
    }

    private fun onLoadCausesSuccess(response: SuccessResponse<List<Cause>>) {
        Timber.tag("QA.V").i("Causes API call success: $response")
        _getCausesStatus = QueroAjudarApiStatus.DONE
        updateFiltersStatus()
        _causes.value = response.data?.toMutableList()
    }

    private fun onLoadSkillsSuccess(response: SuccessResponse<List<Skill>>) {
        Timber.tag("QA.V").i("Causes API call success: $response")
        _getSkillsStatus = QueroAjudarApiStatus.DONE
        updateFiltersStatus()
        _skills.value = response.data?.toMutableList()
    }

    private fun updateFiltersStatus(){
        if(_getCausesStatus == QueroAjudarApiStatus.LOADING
            || _getSkillsStatus == QueroAjudarApiStatus.LOADING){
            _getFiltersStatus.value = QueroAjudarApiStatus.LOADING
        }
        else {
            _getFiltersStatus.value = QueroAjudarApiStatus.DONE
        }
    }
}


