package com.br.queroajudar.vacancies

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class VacanciesPageDataSource @Inject constructor(
    private val scope: CoroutineScope,
    private val remoteDataSource: VacanciesRemoteDataSource,
    private val causes:List<Int>,
    private val skills:List<Int>,
    private val organizationId: Int?
) : PageKeyedDataSource<Int, Vacancy>() {

    val loadInitialResultWrapper = MutableLiveData<ResultWrapper<Any>>()
    val loadAfterResultWrapper = MutableLiveData<ResultWrapper<Any>>()
    val vacanciesSize = MutableLiveData<Int>()

    var getFavorites = false


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Vacancy>) {
        fetchData(1, causes, skills, organizationId, loadInitialResultWrapper){
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
        fetchData(params.key, causes, skills, organizationId, loadAfterResultWrapper){
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {}

    private fun fetchData(page : Int = 1, causes:List<Int> = listOf(), skills:List<Int> = listOf(),
                          organizationId: Int?,
                          resultWrapperLiveData: MutableLiveData<ResultWrapper<Any>>,
                          callback: (List<Vacancy>) -> Unit) {

        scope.launch {
            resultWrapperLiveData.postValue(ResultWrapper.Loading)
            val getVacanciesResponse = if (getFavorites){
                remoteDataSource.fetchFavoriteVacancies(page)
            }
            else {
                remoteDataSource.fetchVacancies(page, causes, skills, organizationId)
            }
            resultWrapperLiveData.postValue(getVacanciesResponse)

            Timber.i("vacancies fetched %s", getVacanciesResponse)

            if(getVacanciesResponse is ResultWrapper.Success){
                val vacancies = getVacanciesResponse.value
                vacanciesSize.postValue(vacanciesSize.value?.plus((vacancies.size)) ?: 0)
                callback(vacancies)
            }
        }
    }
}