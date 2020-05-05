package com.br.queroajudar.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VacancyPageDataSource @Inject constructor(
        private val scope: CoroutineScope,
        private val remoteDataSource: VacancyRemoteDataSource,
        private val causes:String,
        private val skills:String
) : PageKeyedDataSource<Int, Vacancy>() {

//    private val  apiCaller : SafeApiCaller = SafeApiCaller()
    val loadInitialApiStatus = MutableLiveData<ApiStatus>()
    val loadAfterApiStatus = MutableLiveData<ApiStatus>()
    val vacanciesSize = MutableLiveData<Int?>()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Vacancy>) {
        fetchData(1, causes, skills, loadInitialApiStatus){
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
        fetchData(params.key, causes, skills, loadAfterApiStatus){
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {}

    private fun fetchData(page : Int = 1, causes:String = "", skills:String = "",
                          apiStatusLiveData: MutableLiveData<ApiStatus>,
                          callback: (List<Vacancy>) -> Unit) {


        scope.launch {
            apiStatusLiveData.postValue(ApiStatus.LOADING)
            val getVacanciesResponse =
                remoteDataSource.fetchVacancies(page, causes, skills)
            when(getVacanciesResponse) {
                is ResultWrapper.Success -> {
                    apiStatusLiveData.postValue(ApiStatus.DONE)
                    val vacancies = getVacanciesResponse.value.data ?: listOf()
                    vacanciesSize.postValue(vacanciesSize.value?.plus((vacancies.size)) ?: 0)
                    callback(vacancies)
                }
                is ResultWrapper.NetworkError   -> {
                    apiStatusLiveData.postValue(ApiStatus.NETWORK_ERROR)
                }
                is ResultWrapper.GenericError   -> {
                    apiStatusLiveData.postValue(ApiStatus.GENERIC_ERROR)
                }
            }
        }
    }
}