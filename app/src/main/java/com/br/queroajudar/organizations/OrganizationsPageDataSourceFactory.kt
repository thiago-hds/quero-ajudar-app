package com.br.queroajudar.organizations

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.vacancies.VacanciesPageDataSource
import com.br.queroajudar.util.Constants.PAGE_SIZE
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class OrganizationsPageDataSourceFactory @Inject constructor(
    private val scope: CoroutineScope,
    private val dataSource: OrganizationsRemoteDataSource,
    private val getFavorites: Boolean? = false)
    : DataSource.Factory<Int, Organization>() {

    var causes: List<Int> = listOf()
    var skills: List<Int> = listOf()

    val mutableLiveData = MutableLiveData<OrganizationsPageDataSource>()
    lateinit var organizationsPageDataSource : OrganizationsPageDataSource

    override fun create(): DataSource<Int, Organization> {
        organizationsPageDataSource = OrganizationsPageDataSource(
            scope,
            dataSource,
            causes,
            skills
        )
        if (getFavorites != null) {
            organizationsPageDataSource.getFavorites = getFavorites
        }
        mutableLiveData.postValue(organizationsPageDataSource)
        return organizationsPageDataSource
    }

    companion object {
        fun pagedListConfig() = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()
    }
}