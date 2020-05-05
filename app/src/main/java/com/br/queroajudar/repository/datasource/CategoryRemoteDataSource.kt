package com.br.queroajudar.repository.datasource

import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Organization
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchCauses() = apiCaller.safeApiCall(Dispatchers.IO) { service.getCauses()}

    suspend fun fetchSkills() = apiCaller.safeApiCall(Dispatchers.IO) { service.getSkills()}
}