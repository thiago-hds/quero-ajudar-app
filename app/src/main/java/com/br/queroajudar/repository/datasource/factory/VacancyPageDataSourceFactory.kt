package com.br.queroajudar.repository.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.repository.datasource.VacancyPageDataSource
import com.br.queroajudar.repository.datasource.VacancyRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class VacancyPageDataSourceFactory @Inject constructor(
    private val scope: CoroutineScope,
    private val dataSource: VacancyRemoteDataSource,
    val causes :String,
    val skills :String)
    : DataSource.Factory<Int, Vacancy>() {


    val mutableLiveData = MutableLiveData<VacancyPageDataSource>()
    lateinit var vacancyPageDataSource : VacancyPageDataSource

    override fun create(): DataSource<Int, Vacancy> {
        vacancyPageDataSource = VacancyPageDataSource(scope, dataSource, causes, skills)
        mutableLiveData.postValue(vacancyPageDataSource)
        return vacancyPageDataSource
    }

    companion object {
        private const val PAGE_SIZE = 10

        fun pagedListConfig() = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()
    }
}