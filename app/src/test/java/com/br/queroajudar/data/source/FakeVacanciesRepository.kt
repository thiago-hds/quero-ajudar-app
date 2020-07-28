package com.br.queroajudar.data.source

import androidx.annotation.VisibleForTesting
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.util.ItemPagedListing
import kotlinx.coroutines.CoroutineScope

//class FakeVacanciesRepository: VacanciesRepository{
//    val vacancyList = mutableListOf<Vacancy>()
//
//    override fun getPagedVacancies(coroutineScope: CoroutineScope): ItemPagedListing {
        //TODO
//        return VacancyPagedListing(
//            pagedList = livePagedList,
//            loadInitialResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
//                    dataSource -> dataSource.loadInitialResultWrapper
//            },
//            loadAfterResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
//                    dataSource -> dataSource.loadAfterResultWrapper
//            },
//            size = Transformations.switchMap(dataSourceFactory.mutableLiveData){
//                    dataSource -> dataSource.vacanciesSize
//            },
//            refresh = { causesIds: List<Int>, skillsIds: List<Int> ->
//                dataSourceFactory.causes = causesIds
//                dataSourceFactory.skills = skillsIds
//                dataSourceFactory.mutableLiveData.value?.invalidate()
//            }
//        )
//    }
//
//    @VisibleForTesting
//    fun addVacancies(vararg vacancies: Vacancy) {
//        for (vacancy in vacancies) {
//            vacancyList.add(vacancy)
//        }
//    }
//}