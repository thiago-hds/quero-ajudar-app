package com.br.queroajudar.vacancies

import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class VacanciesRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchVacancies(page: Int, causes: List<Int>? = null, skills: List<Int>? = null): ResultWrapper<List<Vacancy>> {

        return apiCaller.safeApiCall(Dispatchers.IO) {
            service.getVacancies(page,causes?.joinToString(),skills?.joinToString())
        }
    }

    suspend fun fetchVacancy(id: Int): ResultWrapper<Vacancy> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.getVacancy(id)
        }
    }

    suspend fun favoriteVacancy(id: Int): ResultWrapper<String> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.postFavoriteVacancy(id)
        }
    }
}