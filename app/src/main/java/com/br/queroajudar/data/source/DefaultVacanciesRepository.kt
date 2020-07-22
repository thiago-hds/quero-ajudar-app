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
            refresh = { causesIds: List<Int>, skillsIds: List<Int>, organizationId: Int? ->
                dataSourceFactory.causes = causesIds
                dataSourceFactory.skills = skillsIds
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