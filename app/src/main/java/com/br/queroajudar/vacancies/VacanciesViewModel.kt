package com.br.queroajudar.vacancies

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.br.queroajudar.data.Category
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.di.CoroutineScopeIO
//import com.br.queroajudar.network.ApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.data.source.DefaultCategoriesRepository
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.util.ItemPagedListing
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

class VacanciesViewModel @Inject constructor(
    private val vacanciesRepository: DefaultVacanciesRepository,
    private val categoriesRepository: DefaultCategoriesRepository,
    private val organizationsRepository: DefaultOrganizationsRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){

    private var _selectedCausesStr = MutableLiveData<String>()
    val selectedCausesStr : LiveData<String>
        get() = _selectedCausesStr
    
    private var _selectedSkillsStr = MutableLiveData<String>()
    val selectedSkillsStr : LiveData<String>
        get() = _selectedSkillsStr


    private var _selectedCausesId = listOf<Int>()
    private var _selectedSkillsId = listOf<Int>()

    lateinit var pagedVacancies: ItemPagedListing<Vacancy>
    lateinit var vacancies: LiveData<PagedList<Vacancy>>
    lateinit var vacanciesSize: LiveData<Int>
    lateinit var vacanciesLoadInitialResultWrapper: LiveData<ResultWrapper<Any>>
    lateinit var vacanciesLoadAfterResultWrapper: LiveData<ResultWrapper<Any>>

    lateinit var organizations: LiveData<ResultWrapper<List<Organization>>>

    lateinit var causes: LiveData<ResultWrapper<List<Category>>>
    lateinit var skills: LiveData<ResultWrapper<List<Category>>>

    init {
        loadVacancies()
        loadOrganizations()
        loadCategories()
    }

    private fun loadVacancies() {
        pagedVacancies = vacanciesRepository.getPagedRecommendedVacancies(coroutineScope)
        vacancies = pagedVacancies.pagedList
        vacanciesLoadInitialResultWrapper = pagedVacancies.loadInitialResultWrapper
        vacanciesLoadAfterResultWrapper = pagedVacancies.loadAfterResultWrapper
        vacanciesSize = pagedVacancies.size
    }

    private fun loadOrganizations(){
        organizations = organizationsRepository.getOrganizations()
    }

    private fun loadCategories(){
        causes = categoriesRepository.getCauses()
        skills = categoriesRepository.getSkills()
    }

    private fun refresh() {
        pagedVacancies?.refresh?.invoke(_selectedCausesId, _selectedSkillsId, null)
    }


    fun onTryAgainClicked(){
        refresh()
    }

    fun onCauseItemSelected(selectedItems : List<Int>) {
        Timber.i("cause selected $selectedItems")
        _selectedCausesId = selectedItems

        (causes.value as ResultWrapper.Success).value?.let{ causeList ->
            val selectedCauses = causeList.filter { selectedItems.contains(it.id) }
            val selectedCausesNames = selectedCauses?.map { it.name }
            _selectedCausesStr.value = selectedCausesNames.joinToString()
        }
        refresh()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.i("skill selected $selectedItems")
        _selectedCausesId = selectedItems

        (causes.value as ResultWrapper.Success).value?.let { skillList ->
            val selectedSkills = skillList.filter { selectedItems.contains(it.id) }
            val selectedSkillsNames = selectedSkills?.map { it.name }
            _selectedSkillsStr.value = selectedSkillsNames.joinToString()
        }
        refresh()
    }
}