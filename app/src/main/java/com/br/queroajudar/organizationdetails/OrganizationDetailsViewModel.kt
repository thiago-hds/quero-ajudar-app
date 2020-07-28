package com.br.queroajudar.organizationdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.properties.Delegates

class OrganizationDetailsViewModel @Inject constructor(
    private val organizationRepository: DefaultOrganizationsRepository,
    private val vacanciesRepository: DefaultVacanciesRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){

    var id by Delegates.notNull<Int>()

    val organization by lazy { organizationRepository.getOrganization(id) }
    val vacancies by lazy { vacanciesRepository.getPagedVacancies(coroutineScope, id).pagedList }



    fun favoriteOrganization(): LiveData<ResultWrapper<Boolean>> {
        return organizationRepository.favoriteOrganization(id)
    }

}