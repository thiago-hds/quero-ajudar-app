package com.br.queroajudar.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.br.queroajudar.repository.datasource.VacancyRemoteDataSource
import com.br.queroajudar.repository.datasource.factory.VacancyPageDataSourceFactory
import com.br.queroajudar.util.VacancyPagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class VacancyRepository @Inject constructor(private val remoteDataSource: VacancyRemoteDataSource) {

    fun observeRemotePagedVacancies(
        coroutineScope: CoroutineScope
    ): VacancyPagedListing {
        val dataSourceFactory = VacancyPageDataSourceFactory(coroutineScope, remoteDataSource)

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacancyPageDataSourceFactory.pagedListConfig()).build()

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
            refresh = { causesIds: List<Int>, skillsIds: List<Int> ->
                dataSourceFactory.causes = causesIds
                dataSourceFactory.skills = skillsIds
                dataSourceFactory.mutableLiveData.value?.invalidate()
            }
        )
    }
}