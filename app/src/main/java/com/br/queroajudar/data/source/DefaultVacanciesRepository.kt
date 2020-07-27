package com.br.queroajudar.data.source

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.br.queroajudar.util.VacancyPagedListing
import com.br.queroajudar.util.resultLiveData
import com.br.queroajudar.vacancies.VacanciesPageDataSourceFactory
import com.br.queroajudar.vacancies.VacanciesRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class DefaultVacanciesRepository @Inject constructor(
    private val remoteDataSource: VacanciesRemoteDataSource
): VacanciesRepository {

    override fun getPagedVacancies(
        coroutineScope: CoroutineScope,
        organizationId : Int?
    ): VacancyPagedListing {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                organizationId
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()
        ).build()

        return VacancyPagedListing(
            pagedList = livePagedList,
            loadInitialResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadInitialResultWrapper
            },
            loadAfterResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadAfterResultWrapper
            },
            size = Transformations.switchMap(dataSourceFactory.mutableLiveData){
                dataSource -> dataSource.vacanciesSize
            },
            refresh = { causesIds: List<Int>?, skillsIds: List<Int>?, organizationId: Int? ->
                causesIds?.let { dataSourceFactory.causes = it }
                skillsIds?.let{ dataSourceFactory.skills = it }
                dataSourceFactory.mutableLiveData.value?.invalidate()
            }
        )
    }

    fun getFavoritePagedVacancies(coroutineScope: CoroutineScope): VacancyPagedListing {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                getFavorites = true
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()
        ).build()

        return VacancyPagedListing(
            pagedList = livePagedList,
            loadInitialResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadInitialResultWrapper
            },
            loadAfterResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadAfterResultWrapper
            },
            size = Transformations.switchMap(dataSourceFactory.mutableLiveData){
                    dataSource -> dataSource.vacanciesSize
            },
            refresh = { causesIds: List<Int>?, skillsIds: List<Int>?, organizationId: Int? ->
                dataSourceFactory.mutableLiveData.value?.invalidate()
            }
        )
    }

    fun getVacancy(id: Int) = resultLiveData(
        networkCall = {remoteDataSource.fetchVacancy(id)}
    )

    fun favoriteVacancy(id: Int) = resultLiveData(
        networkCall = {remoteDataSource.favoriteVacancy(id)}
    )
}