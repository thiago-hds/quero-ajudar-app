package com.br.queroajudar.organizations

import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class OrganizationsRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller,
    @CoroutineScopeIO private val scope: CoroutineScope

) {

//    fun fetchOrganizations(page: Int = 1) : ResultWrapper<SuccessResponse<List<Organization>>>? {
//        var result: ResultWrapper<SuccessResponse<List<Organization>>>? = null
//        scope.launch {
//            result = apiCaller.safeApiCall(Dispatchers.IO) {
//                service.getOrganizations(page)
//            }
//        }
//        return result
//    }

    suspend fun fetchOrganizations(page: Int = 1) =
        apiCaller.safeApiCall(Dispatchers.IO) {
            service.getOrganizations(page)
        }
}