package com.br.queroajudar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.repository.datasource.factory.VacancyDataSourceFactory
import com.br.queroajudar.util.Listing
import com.br.queroajudar.util.PagedListing
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class VacancyRepository @Inject constructor() {


//    fun observePagedVacancies(coroutineScope: CoroutineScope) = observeRemotePagedVacancies(coroutineScope)
////        if (connectivityAvailable) observeRemotePagedSets(themeId, coroutineScope)
////        else observeLocalPagedSets(themeId)

    fun observeRemotePagedVacancies(
        coroutineScope: CoroutineScope,
        skills: String,
        causes: String
    ): PagedListing<Vacancy> {
        val dataSourceFactory = VacancyDataSourceFactory(coroutineScope, skills, causes)
//        dataSourceFactory.skills = skills
//        dataSourceFactory.causes = causes

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            VacancyDataSourceFactory.pagedListConfig()).build()

        return PagedListing(
            pagedList = livePagedList,
            loadInitialApiStatus = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadInitialApiStatus
            },
            loadAfterApiStatus = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadAfterApiStatus
            },
            refresh = {
                dataSourceFactory.mutableLiveData.value?.invalidate()
            }
        )
    }

//    fun setCauses(causes:String){
//        dataSourceFactory.causes = causes
//    }
//
//    fun setSkills(skills:String){
//        dataSourceFactory.skills = skills
//
//    }
//
//    fun refresh(){
//        dataSourceFactory.mutableLiveData.value?.invalidate()
//    }
}