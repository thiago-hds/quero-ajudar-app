package com.br.queroajudar.categories

import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoriesRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun fetchCauses() = apiCaller.safeApiCall(Dispatchers.IO) { service.getCauses()}

    suspend fun fetchSkills() = apiCaller.safeApiCall(Dispatchers.IO) { service.getSkills()}

    suspend fun postUserCauses(causesIds: List<Int>) = apiCaller.safeApiCall(Dispatchers.IO){
        service.postUpdateUserCauses(causesIds)
    }

    suspend fun postUserSkills(skillsIds: List<Int>) = apiCaller.safeApiCall(Dispatchers.IO) {
        service.postUpdateUserSkills(skillsIds)
    }
}