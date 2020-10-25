package com.br.queroajudar.vacancies

import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.formdata.VacancyApplicationData
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class VacanciesRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchVacancies(page: Int,
                               causes: List<Int>? = null, skills: List<Int>? = null,
                               organizationId: Int?): ResultWrapper<List<Vacancy>> {

        return apiCaller.safeApiCall(Dispatchers.IO) {
            service.getVacancies(page,causes?.joinToString(),skills?.joinToString(), organizationId)
        }
    }

    suspend fun fetchRecommendedVacancies(page:Int,
                                          causes: List<Int>? = null, skills: List<Int>? = null,
                                          organizationId: Int?): ResultWrapper<List<Vacancy>>{
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.getVacanciesRecommendations(
                page,causes?.joinToString(),skills?.joinToString(), organizationId
            )
        }
    }

    suspend fun fetchFavoriteVacancies(page:Int): ResultWrapper<List<Vacancy>>{
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.getFavoriteVacancies(page)
        }
    }

    suspend fun fetchVacancy(id: Int): ResultWrapper<Vacancy> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.getVacancy(id)
        }
    }

    suspend fun favoriteVacancy(id: Int): ResultWrapper<Boolean> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.postFavoriteVacancy(id)
        }
    }

    suspend fun applyForVacancy(data: VacancyApplicationData): ResultWrapper<Boolean> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.postApplication(data)
        }
    }

    suspend fun cancelApplicationForVacancy(applicationId: Int): ResultWrapper<Boolean> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.deleteApplication(applicationId)
        }
    }
}