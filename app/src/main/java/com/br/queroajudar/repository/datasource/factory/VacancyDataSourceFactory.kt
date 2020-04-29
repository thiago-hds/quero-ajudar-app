package com.br.queroajudar.repository.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.repository.datasource.VacancyDataSource
import kotlinx.coroutines.CoroutineScope

class VacancyDataSourceFactory(private val scope: CoroutineScope, val causes :String, val skills :String)
    : DataSource.Factory<Int, Vacancy>() {


    val mutableLiveData = MutableLiveData<VacancyDataSource>()
    lateinit var vacancyDataSource : VacancyDataSource

    override fun create(): DataSource<Int, Vacancy> {
        vacancyDataSource = VacancyDataSource(scope, causes, skills)
        mutableLiveData.postValue(vacancyDataSource)
        return vacancyDataSource
    }

    companion object {
        private const val PAGE_SIZE = 10

        fun pagedListConfig() = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()
    }
}