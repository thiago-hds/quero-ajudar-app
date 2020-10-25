package com.br.queroajudar.data.source

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.formdata.VacancyApplicationData
import com.br.queroajudar.util.ItemPagedListing
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
    ): ItemPagedListing<Vacancy> {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                organizationId = organizationId
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()
        ).build()

        return ItemPagedListing(
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

    fun getPagedRecommendedVacancies(
        coroutineScope: CoroutineScope
    ): ItemPagedListing<Vacancy> {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                getRecommended = true
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()
        ).build()

        return ItemPagedListing(
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


    fun getPagedFavoriteVacancies(coroutineScope: CoroutineScope): ItemPagedListing<Vacancy> {
        val dataSourceFactory =
            VacanciesPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                getFavorites = true
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacanciesPageDataSourceFactory.pagedListConfig()
        ).build()

        return ItemPagedListing(
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

    fun applyForVacancy(data: VacancyApplicationData) = resultLiveData(
        networkCall = {remoteDataSource.applyForVacancy(data)}
    )

    fun cancelApplicationForVacancy(applicationId: Int) = resultLiveData(
        networkCall = {remoteDataSource.cancelApplicationForVacancy(applicationId)}
    )
}