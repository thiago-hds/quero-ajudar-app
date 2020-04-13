package com.br.queroajudar.viewmodel

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.QueroAjudarRepository
import com.br.queroajudar.repository.VacancyDataFactory
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
    lateinit var vacancies : LiveData<PagedList<Vacancy>>
    lateinit var vacanciesLoadInitialApiStatus: LiveData<QueroAjudarApiStatus>
    lateinit var vacanciesLoadAfterApiStatus: LiveData<QueroAjudarApiStatus>
    lateinit var vacanciesSize : LiveData<Int?>


    private val _getFiltersStatus = MutableLiveData<QueroAjudarApiStatus>()
    val getFiltersStatus: LiveData<QueroAjudarApiStatus>
        get() = _getFiltersStatus



    private var _causes = MutableLiveData<MutableList<Category>>()
    val causes : LiveData<MutableList<Category>>
        get() = _causes

    private var _skills = MutableLiveData<List<Category>>()
    val skills : LiveData<List<Category>>
        get() = _skills

    private var _selectedCausesStr = MutableLiveData<String>()
    val selectedCausesStr : LiveData<String>
        get() = _selectedCausesStr
    
    private var _selectedSkillsStr = MutableLiveData<String>()
    val selectedSkillsStr : LiveData<String>
        get() = _selectedSkillsStr

    // Variaveis de controle
    private var _page = 1
    private var _selectedCausesId = listOf<Int>()
    private var _selectedSkillsId = listOf<Int>()

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
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()
        vacancies = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Vacancy> {

        vacancyDataFactory = VacancyDataFactory(coroutineScope)

        vacanciesSize = Transformations.switchMap(vacancyDataFactory.mutableLiveData){
            dataSource -> dataSource.vacanciesSize
        }

        vacanciesLoadInitialApiStatus = Transformations.switchMap(vacancyDataFactory.mutableLiveData) {
                dataSource -> dataSource.vacanciesLoadInitialApiStatus
        }

        vacanciesLoadAfterApiStatus = Transformations.switchMap(vacancyDataFactory.mutableLiveData) {
            dataSource -> dataSource.vacanciesLoadAfterApiStatus
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

    fun onTryAgainClicked(){
        vacancyDataFactory.mutableLiveData.value?.invalidate()
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
        _selectedCausesId = selectedItems

        val selectedCauses = _causes.value?.filter { selectedItems.contains(it.id) }
        val selectedCausesNames = selectedCauses?.map { it.name } ?: listOf()
        _selectedCausesStr.value = selectedCausesNames.joinToString()


        vacancyDataFactory.causes = _selectedCausesId.joinToString()
        vacancyDataFactory.mutableLiveData.value?.invalidate()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.tag("QA.VacanciesVM").i("skill selected $selectedItems")
        _selectedCausesId = selectedItems

        val selectedSkills = _skills.value?.filter { selectedItems.contains(it.id) }
        val selectedSkillsNames = selectedSkills?.map { it.name } ?: listOf()
        _selectedSkillsStr.value = selectedSkillsNames.joinToString()
        
        vacancyDataFactory.skills = _selectedSkillsId.joinToString()
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



