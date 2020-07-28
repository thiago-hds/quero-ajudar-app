package com.br.queroajudar.organizations

import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class OrganizationsRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller,
    @CoroutineScopeIO private val scope: CoroutineScope

) {

    suspend fun fetchOrganizations(page: Int = 1, causes: List<Int>? = null, skills: List<Int>? = null) =
        apiCaller.safeApiCall(Dispatchers.IO) {
            service.getOrganizations(page)
        }

    suspend fun fetchFavoriteOrganizations(page: Int = 1) =
        apiCaller.safeApiCall(Dispatchers.IO) {
            service.getFavoriteOrganizations(page)
        }

    suspend fun fetchOrganization(id: Int): ResultWrapper<Organization> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.getOrganization(id)
        }
    }

    suspend fun favoriteOrganization(id: Int): ResultWrapper<Boolean> {
        return apiCaller.safeApiCall(Dispatchers.IO){
            service.postFavoriteOrganization(id)
        }
    }
}