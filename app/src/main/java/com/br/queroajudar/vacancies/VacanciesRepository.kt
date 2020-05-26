package com.br.queroajudar.vacancies

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.br.queroajudar.util.VacancyPagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class VacanciesRepository @Inject constructor(private val remoteDataSource: VacanciesRemoteDataSource) {

    fun observeRemotePagedVacancies(
        coroutineScope: CoroutineScope
    ): VacancyPagedListing {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()).build()

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