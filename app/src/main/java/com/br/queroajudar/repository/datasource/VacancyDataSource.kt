package com.br.queroajudar.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VacancyDataSource(private val scope: CoroutineScope,
                        private val causes:String,
                        private val skills:String
) : PageKeyedDataSource<Int, Vacancy>() {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()
    val loadInitialApiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val loadAfterApiStatus = MutableLiveData<QueroAjudarApiStatus>()
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
                          apiStatusLiveData: MutableLiveData<QueroAjudarApiStatus>,
                          callback: (List<Vacancy>) -> Unit) {


        scope.launch {
            apiStatusLiveData.value = QueroAjudarApiStatus.LOADING
            val getVacanciesResponse =
                apiCaller.safeApiCall(Dispatchers.IO) {
                    QueroAjudarApi.retrofitService.getVacancies(page, causes, skills)
                }
            when(getVacanciesResponse) {
                is ResultWrapper.Success -> {
                    apiStatusLiveData.value = QueroAjudarApiStatus.DONE
                    val vacancies = getVacanciesResponse.value.data ?: listOf()
                    vacanciesSize.value = vacanciesSize.value?.plus((vacancies.size)) ?: 0
                    callback(vacancies)
                }
                is ResultWrapper.NetworkError   -> {
                    apiStatusLiveData.value = QueroAjudarApiStatus.NETWORK_ERROR
                }
                is ResultWrapper.GenericError   -> {
                    apiStatusLiveData.value = QueroAjudarApiStatus.GENERIC_ERROR
                }
            }
        }
    }
}