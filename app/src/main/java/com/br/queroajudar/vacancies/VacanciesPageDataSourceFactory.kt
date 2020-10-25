package com.br.queroajudar.vacancies

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.vacancies.VacanciesPageDataSource
import com.br.queroajudar.vacancies.VacanciesRemoteDataSource
import com.br.queroajudar.util.Constants.PAGE_SIZE
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class VacanciesPageDataSourceFactory @Inject constructor(
    private val scope: CoroutineScope,
    private val dataSource: VacanciesRemoteDataSource,
    private val organizationId: Int? = null,
    private val getFavorites: Boolean? = false,
    private val getRecommended: Boolean? = false)
    : DataSource.Factory<Int, Vacancy>() {

    var causes: List<Int> = listOf()
    var skills: List<Int> = listOf()

    val mutableLiveData = MutableLiveData<VacanciesPageDataSource>()
    lateinit var vacanciesPageDataSource : VacanciesPageDataSource

    override fun create(): DataSource<Int, Vacancy> {
        vacanciesPageDataSource = VacanciesPageDataSource(
                scope,
                dataSource,
                causes,
                skills,
                organizationId
        )
        if (getFavorites != null) {
            vacanciesPageDataSource.getFavorites = getFavorites
        }
        if (getRecommended != null) {
            vacanciesPageDataSource.getRecommended = getRecommended
        }
        mutableLiveData.postValue(vacanciesPageDataSource)
        return vacanciesPageDataSource
    }

    companion object {
        fun pagedListConfig() = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()
    }
}