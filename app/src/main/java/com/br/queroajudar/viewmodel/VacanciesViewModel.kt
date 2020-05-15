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
    private val vacancyRepository: VacancyRepository,
    private val categoryRepository: CategoryRepository,
    private val organizationRepository: OrganizationRepository,
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

    private var repoResult = vacancyRepository.observeRemotePagedVacancies(coroutineScope)
    var vacancies = repoResult.pagedList
    var vacanciesLoadInitialResultWrapper = repoResult.loadInitialResultWrapper
    var vacanciesLoadAfterResultWrapper = repoResult.loadAfterResultWrapper
    var vacanciesSize = repoResult.size

    val organizations = organizationRepository.organizations
    val causes = categoryRepository.causes
    val skills = categoryRepository.skills

    /*
     *  Eventos da UI
     */

    fun onVacancyClicked(){
        //TODO
    }

    fun onTryAgainClicked(){
        refresh()
    }

    fun onCauseItemSelected(selectedItems : List<Int>) {
        Timber.i("cause selected $selectedItems")
        _selectedCausesId = selectedItems

        (causes.value as ResultWrapper.Success).value.data?.let{ causeList ->
            val selectedCauses = causeList.filter { selectedItems.contains(it.id) }
            val selectedCausesNames = selectedCauses?.map { it.name }
            _selectedCausesStr.value = selectedCausesNames.joinToString()
        }
        refresh()
    }

    fun onSkillItemSelected(selectedItems : List<Int>) {
        Timber.i("skill selected $selectedItems")
        _selectedCausesId = selectedItems

        (causes.value as ResultWrapper.Success).value.data?.let { skillList ->
            val selectedSkills = skillList.filter { selectedItems.contains(it.id) }
            val selectedSkillsNames = selectedSkills?.map { it.name }
            _selectedSkillsStr.value = selectedSkillsNames.joinToString()
        }
        refresh()
    }

    fun refresh() {
        repoResult?.refresh?.invoke(_selectedCausesId, _selectedSkillsId)
    }
}