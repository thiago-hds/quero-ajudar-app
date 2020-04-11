package com.br.queroajudar.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class VacancyRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()

    suspend fun getVacancies(
        page : Int,
        causes:String = "",
        skills:String = "") : ResultWrapper<SuccessResponse<List<Vacancy>>> {
        return apiCaller.safeApiCall(dispatcher) {
            QueroAjudarApi.retrofitService.getVacancies(page, causes, skills)
        }
    }
}

class VacancyDataFactory(private val scope:CoroutineScope) : DataSource.Factory<Int, Vacancy>() {
    val mutableLiveData = MutableLiveData<VacancyDataSource>()
    lateinit var vacancyDataSource : VacancyDataSource
    var causes = ""
    var skills = ""

    override fun create(): DataSource<Int, Vacancy> {
        Timber.tag("QA.VacancyRepository").i("Create called")
        vacancyDataSource = VacancyDataSource(scope, causes, skills)
        mutableLiveData.postValue(vacancyDataSource)
        return vacancyDataSource
    }
}

class VacancyDataSource(private val scope:CoroutineScope,
                        private val causes:String,
                        private val skills:String
                        ) : PageKeyedDataSource<Int, Vacancy>() {

    private val repository = VacancyRepository()
    val vacanciesLoadInitialApiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val vacanciesLoadAfterApiStatus = MutableLiveData<QueroAjudarApiStatus>()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Vacancy>
    ) {

        scope.launch {
            vacanciesLoadInitialApiStatus.value = QueroAjudarApiStatus.LOADING
            when (val getVacanciesResponse =
                repository.getVacancies(1, causes, skills)) {
                is ResultWrapper.Success -> {
                    vacanciesLoadInitialApiStatus.value = QueroAjudarApiStatus.DONE
                    val vacancies = getVacanciesResponse.value.data
                    callback.onResult(vacancies ?: listOf(), null, 2)
                }
                is ResultWrapper.NetworkError   ->
                    vacanciesLoadInitialApiStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
                is ResultWrapper.GenericError   ->
                    vacanciesLoadInitialApiStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {

        scope.launch {
            vacanciesLoadAfterApiStatus.value = QueroAjudarApiStatus.LOADING
            when (val getVacanciesResponse =
                repository.getVacancies(params.key, causes, skills)) {
                is ResultWrapper.Success -> {
                    vacanciesLoadAfterApiStatus.value = QueroAjudarApiStatus.DONE
                    val vacancies = getVacanciesResponse.value.data
                    callback.onResult(vacancies ?: listOf(), params.key + 1)
                }
                is ResultWrapper.NetworkError   ->
                    vacanciesLoadAfterApiStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
                is ResultWrapper.GenericError   ->
                    vacanciesLoadAfterApiStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
    }
}