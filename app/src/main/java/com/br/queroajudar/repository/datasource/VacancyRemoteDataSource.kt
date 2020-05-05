package com.br.queroajudar.repository.datasource

import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class VacancyRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchVacancies(page: Int, causes: String? = null, skills: String? = null): ResultWrapper<SuccessResponse<List<Vacancy>>> {
        return apiCaller.safeApiCall(Dispatchers.IO) {
            service.getVacancies(page,causes,skills)
        }
    }
}