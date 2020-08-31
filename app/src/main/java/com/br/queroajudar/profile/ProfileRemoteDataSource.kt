package com.br.queroajudar.profile

import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchProfile() = apiCaller.safeApiCall(Dispatchers.IO) { service.getProfile()}
}