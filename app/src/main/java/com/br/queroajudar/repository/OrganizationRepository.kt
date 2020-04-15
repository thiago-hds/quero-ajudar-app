package com.br.queroajudar.repository

import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Organization
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OrganizationRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()

    suspend fun getOrganizations(page:Int = 1) : ResultWrapper<SuccessResponse<List<Organization>>> {
        return apiCaller.safeApiCall(dispatcher) {
            QueroAjudarApi.retrofitService.getOrganizations(page)
        }
    }
}