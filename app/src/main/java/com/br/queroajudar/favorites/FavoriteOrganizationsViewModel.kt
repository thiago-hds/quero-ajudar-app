package com.br.queroajudar.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.ItemPagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class FavoriteOrganizationsViewModel @Inject constructor(
    private val repository: DefaultOrganizationsRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel() {

    lateinit var pagedOrganizations: ItemPagedListing<Organization>
    lateinit var organizations: LiveData<PagedList<Organization>>
    lateinit var organizationsLoadInitialResultWrapper: LiveData<ResultWrapper<Any>>
    lateinit var organizationsLoadAfterResultWrapper: LiveData<ResultWrapper<Any>>

    init{
        loadOrganizations()
    }

    private fun loadOrganizations() {
        pagedOrganizations = repository.getPagedFavoriteOrganizations(coroutineScope)
        organizations = pagedOrganizations.pagedList
        organizationsLoadInitialResultWrapper = pagedOrganizations.loadInitialResultWrapper
        organizationsLoadAfterResultWrapper = pagedOrganizations.loadAfterResultWrapper
        //organizationsSize = pagedOrganizations.size
    }

}