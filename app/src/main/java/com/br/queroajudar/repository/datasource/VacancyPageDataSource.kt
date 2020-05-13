package com.br.queroajudar.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.*
import com.br.queroajudar.util.resultLiveData
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

//    val loadInitialApiStatus = MutableLiveData<ApiStatus>()
//    val loadAfterApiStatus = MutableLiveData<ApiStatus>()
//    val vacanciesSize = MutableLiveData<Int?>()

    val loadInitialResultWrapper = MutableLiveData<ResultWrapper<Any>>()
    val loadAfterResultWrapper = MutableLiveData<ResultWrapper<Any>>()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Vacancy>) {
        fetchData(1, causes, skills, loadInitialResultWrapper){
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
        fetchData(params.key, causes, skills, loadAfterResultWrapper){
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {}

    private fun fetchData(page : Int = 1, causes:String = "", skills:String = "",
                          resultWrapperLiveData: MutableLiveData<ResultWrapper<Any>>,
                          callback: (List<Vacancy>) -> Unit) {

        scope.launch {
            resultWrapperLiveData.postValue(ResultWrapper.Loading)
            val getVacanciesResponse =
                remoteDataSource.fetchVacancies(page, causes, skills)
            resultWrapperLiveData.postValue(getVacanciesResponse)

            if(getVacanciesResponse is ResultWrapper.Success){
                //apiStatusLiveData.postValue(ApiStatus.DONE)
                val vacancies = getVacanciesResponse.value.data ?: listOf()
                //vacanciesSize.postValue(vacanciesSize.value?.plus((vacancies.size)) ?: 0)
                callback(vacancies)
            }
        }
    }
}