package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.QueroAjudarRepository
import com.br.queroajudar.repository.VacancyDataFactory
import com.br.queroajudar.repository.VacancyDataSource
import com.br.queroajudar.repository.VacancyRepository
import com.br.queroajudar.util.append
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class VacanciesViewModel : ViewModel(){

    // Repositorios
    private val globalRepository = QueroAjudarRepository()

    private lateinit var vacancyDataFactory : VacancyDataFactory
    // Dados observaveis
    //private val _getVacanciesStatus = MutableLiveData<QueroAjudarApiStatus>()
    lateinit var getVacanciesStatus: LiveData<QueroAjudarApiStatus>

    private val _getFiltersStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getFiltersStatus: LiveData<QueroAjudarApiStatus>
        get() = _getFiltersStatus

    private var _vacancies = MutableLiveData<PagedList<Vacancy>>()
    lateinit var vacancies : LiveData<PagedList<Vacancy>>

    private var _causes = MutableLiveData<MutableList<Category>>()
    val causes : LiveData<MutableList<Category>>
        get() = _causes

    private var _skills = MutableLiveData<List<Category>>()
    val skills : LiveData<List<Category>>
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
        _causes.value = mutableListOf()
        _skills.value = mutableListOf()

        _getFiltersStatus.value     = QueroAjudarApiStatus.LOADING

        initPaging()

//        loadVacancies()
    }

    private fun initPaging(){
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()
        vacancies = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Vacancy> {

        vacancyDataFactory = VacancyDataFactory(coroutineScope)

        getVacanciesStatus = Transformations.switchMap(vacancyDataFactory.mutableLiveData) {
            dataSource -> dataSource.vacanciesApiStatus
        }

        return LivePagedListBuilder(vacancyDataFactory, config)
    }


    /*
     *  Eventos da UI
     */

    fun onListScrollReachEnd(){
//        if(_getVacanciesStatus.value != QueroAjudarApiStatus.LOADING && !_endLoading) {
//            loadVacancies()
//        }
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
        Timber.tag("QA.VacanciesVM").i("cause selected ${selectedItems.toString()}")
        _selectedCauses = selectedItems
        vacancyDataFactory.causes = _selectedCauses.joinToString()
        vacancyDataFactory.mutableLiveData.value?.invalidate()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.tag("QA.VacanciesVM").i("skill selected $selectedItems")

        _selectedCauses = selectedItems
        vacancyDataFactory.skills = _selectedSkills.joinToString()
        vacancyDataFactory.mutableLiveData.value?.invalidate()
    }


//    private fun loadVacancies(){
//        Timber.tag("QA.VacanciesVM").i("Loading vacancies page $_page")
//        val strCauses = _selectedCauses.joinToString()
//        val strSkills = _selectedSkills.joinToString ()
//
//        _getVacanciesStatus.value = QueroAjudarApiStatus.LOADING
//        coroutineScope.launch {
//            when (val getVacanciesResponse =
//                    vacancyRepository.getVacancies(_page, strCauses, strSkills)) {
//                is ResultWrapper.Success -> onLoadVacanciesSuccess(getVacanciesResponse.value)
//                is ResultWrapper.NetworkError   ->
//                    _getVacanciesStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
//                is ResultWrapper.GenericError   ->
//                    _getVacanciesStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
//            }
//        }
//    }

    private fun loadCauses(){
        Timber.tag("QA.VacanciesVM").i("Loading causes")
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
        Timber.tag("QA.VacanciesVM").i("Loading skills")
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

//    private fun onLoadVacanciesSuccess(value: SuccessResponse<List<Vacancy>>) {
//        Timber.tag("QA.VacanciesVM").i("Vacancies API call success: $value")
//        val newVacancies = value.data
//        if(newVacancies?.isNotEmpty()!!){
//            _vacancies.append(newVacancies)
//            _page ++
//        }
//        else{
//            _endLoading = true
//        }
//        _getVacanciesStatus.value = QueroAjudarApiStatus.DONE
//    }

    private fun onLoadCausesSuccess(response: SuccessResponse<List<Category>>) {
        Timber.tag("QA.VacanciesVM").i("Causes API call success: $response")
        _getCausesStatus = QueroAjudarApiStatus.DONE
        updateFiltersStatus()
        _causes.value = response.data?.toMutableList()
    }

    private fun onLoadSkillsSuccess(response: SuccessResponse<List<Category>>) {
        Timber.tag("QA.VacanciesVM").i("Causes API call success: $response")
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



