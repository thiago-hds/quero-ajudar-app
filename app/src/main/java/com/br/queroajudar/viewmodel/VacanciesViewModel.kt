package com.br.queroajudar.viewmodel

import androidx.lifecycle.*
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Organization
//import com.br.queroajudar.network.ApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.OrganizationRepository
import com.br.queroajudar.repository.CategoryRepository
import com.br.queroajudar.repository.VacancyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class VacanciesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val vacancyRepository: VacancyRepository,
    private val organizationRepository: OrganizationRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){


//    private val _getFiltersStatus = MutableLiveData<ApiStatus>()
//    val getFiltersStatus: LiveData<ApiStatus>
//        get() = _getFiltersStatus
//
//    private var _getOrganizationsStatus = MutableLiveData<ApiStatus>()
//    val getOrganizationsStatus : LiveData<ApiStatus>
//        get() = _getOrganizationsStatus


//    private var _causes = MutableLiveData<List<Category>>()
//    val causes : LiveData<List<Category>>
//        get() = _causes
//
//    private var _skills = MutableLiveData<List<Category>>()
//    val skills : LiveData<List<Category>>
//        get() = _skills

//    private var _organizations = MutableLiveData<List<Organization>>()
//    val organizations : LiveData<List<Organization>>
//        get() = _organizations

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
//    private var _getCausesStatus = ApiStatus.DONE
//    private var _getSkillsStatus = ApiStatus.DONE

    // Escopo de Corotina
//    private var viewModelJob = Job()
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var strSelectedCausesId = ""
    private var strSelectedSkillsId = ""

    // Repositorios
//    private val globalRepository = QueroAjudarRepository()
//    private val organizationRepository = OrganizationRepository()
//    private val vacancyRepository = VacancyRepository(coroutineScope)


//    val vacanciesRepoResult = vacancyRepository.observePagedVacancies()

    private val repoResult = vacancyRepository.observeRemotePagedVacancies(coroutineScope,"","")

    val vacancies = repoResult.pagedList
    val vacanciesLoadInitialResultWrapper = repoResult.loadInitialResultWrapper
    val vacanciesLoadAfterResultWrapper = repoResult.loadAfterResultWrapper

    val organizations = organizationRepository.organizations

    val causes = categoryRepository.causes

    val skills = categoryRepository.skills


    init{
//        _causes.value = mutableListOf()
//        _skills.value = mutableListOf()

//        _getFiltersStatus.value = ApiStatus.DONE

//        initPaging()
//        loadOrganizations()
//        initializedPagedListBuilder()
    }

//    private fun initPaging(){
//        val config = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(10)
//            .build()
//        vacancies = initializedPagedListBuilder(config).build()
//    }

//    private fun initializedPagedListBuilder(){
//
//
//        vacanciesSize = Transformations.switchMap(vacancyRepository.dataSourceFactory.mutableLiveData){
//            dataSource -> dataSource.vacanciesSize
//        }
//
//        vacanciesLoadInitialApiStatus = Transformations.switchMap(vacancyRepository.dataSourceFactory.mutableLiveData) {
//                dataSource -> dataSource.vacanciesLoadInitialApiStatus
//        }
//
//        vacanciesLoadAfterApiStatus = Transformations.switchMap(vacancyRepository.dataSourceFactory.mutableLiveData) {
//            dataSource -> dataSource.vacanciesLoadAfterApiStatus
//        }
//
//    }


    /*
     *  Eventos da UI
     */

    fun onVacancyClicked(){
        //TODO
    }

    fun onTryAgainClicked(){
//        vacancyDataSourceFactory.mutableLiveData.value?.invalidate()
//        vacancyRepository.refresh()
    }

    fun onDrawerOpened(){
        // Carregar filtros
//        if(causes.value.isNullOrEmpty() || skills.value.isNullOrEmpty()){
//            loadCauses()
//            loadSkills()
//        }
    }

    fun refresh() {
        repoResult?.refresh?.invoke()
    }

    fun onCauseItemSelected(selectedItems : List<Int>) {
        Timber.i("cause selected ${selectedItems.toString()}")
        _selectedCausesId = selectedItems

//        val selectedCauses = _causes.value?.filter { selectedItems.contains(it.id) }
//        val selectedCausesNames = selectedCauses?.map { it.name } ?: listOf()
//        _selectedCausesStr.value = selectedCausesNames.joinToString()

        //vacancyRepository.setCauses(_selectedCausesId.joinToString())
        refresh()

//        vacancyDataSourceFactory.causes = _selectedCausesId.joinToString()
//        vacancyDataSourceFactory.mutableLiveData.value?.invalidate()

//        vacancyRepository.setCauses(_selectedCausesId.joinToString())
//        strSelectedCausesId =_selectedCausesId.joinToString()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.i("skill selected $selectedItems")
        _selectedCausesId = selectedItems

//        val selectedSkills = _skills.value?.filter { selectedItems.contains(it.id) }
//        val selectedSkillsNames = selectedSkills?.map { it.name } ?: listOf()
//        _selectedSkillsStr.value = selectedSkillsNames.joinToString()

        //vacancyRepository.setSkills(_selectedSkillsId.joinToString())
        refresh()
        
//        vacancyDataSourceFactory.skills = _selectedSkillsId.joinToString()
//        vacancyDataSourceFactory.mutableLiveData.value?.invalidate()
//        vacancyRepository.setSkills(_selectedSkillsId.joinToString())

//        strSelectedSkillsId =_selectedSkillsId.joinToString()
    }

//    private fun loadCauses(){
//        Timber.i("Loading causes")
//        _getCausesStatus = ApiStatus.LOADING
//        updateFiltersStatus()
//        coroutineScope.launch {
//            when (val getCausesResponse = globalRepository.getCauses()) {
//                is ResultWrapper.Success        -> onLoadCausesSuccess(getCausesResponse.value)
//                is ResultWrapper.NetworkError   -> _getCausesStatus = ApiStatus.NETWORK_ERROR
//                is ResultWrapper.GenericError   -> _getCausesStatus = ApiStatus.GENERIC_ERROR
//            }
//        }
//    }
//
//    private fun loadSkills(){
//        Timber.i("Loading skills")
//        _getSkillsStatus = ApiStatus.LOADING
//        updateFiltersStatus()
//        coroutineScope.launch {
//            when (val getSkillsResponse = globalRepository.getSkills()) {
//                is ResultWrapper.Success        -> onLoadSkillsSuccess(getSkillsResponse.value)
//                is ResultWrapper.NetworkError   -> _getSkillsStatus = ApiStatus.NETWORK_ERROR
//                is ResultWrapper.GenericError   -> _getSkillsStatus = ApiStatus.GENERIC_ERROR
//            }
//        }
//    }

//    private fun loadOrganizations(){
//        Timber.i("Loading organizations")
//        _getOrganizationsStatus.value = ApiStatus.LOADING
//        coroutineScope.launch {
//            when (val getOrganizationsResponse = organizationRepository.getOrganizations()) {
//                is ResultWrapper.Success        -> onLoadOrganizationsSuccess(getOrganizationsResponse.value)
//                is ResultWrapper.NetworkError   -> _getOrganizationsStatus.postValue(ApiStatus.NETWORK_ERROR)
//                is ResultWrapper.GenericError   -> _getOrganizationsStatus.postValue(ApiStatus.GENERIC_ERROR)
//            }
//        }
//    }

//    private fun onLoadCausesSuccess(response: SuccessResponse<List<Category>>) {
//        Timber.i("Causes API call success: $response")
//        _getCausesStatus = ApiStatus.DONE
//        updateFiltersStatus()
//        _causes.value = response.data
//    }
//
//    private fun onLoadSkillsSuccess(response: SuccessResponse<List<Category>>) {
//        Timber.i("Causes API call success: $response")
//        _getSkillsStatus = ApiStatus.DONE
//        updateFiltersStatus()
//        _skills.value = response.data
//    }

//    private fun onLoadOrganizationsSuccess(response: SuccessResponse<List<Organization>>) {
//        Timber.i("Organizations API call success: $response")
//        _getOrganizationsStatus.value = ApiStatus.DONE
//        _organizations.value = response.data
//    }

//    private fun updateFiltersStatus(){
//        if(_getCausesStatus == ApiStatus.LOADING
//            || _getSkillsStatus == ApiStatus.LOADING){
//            _getFiltersStatus.value = ApiStatus.LOADING
//        }
//        else {
//            _getFiltersStatus.value = ApiStatus.DONE
//        }
//    }
}