package com.br.queroajudar.data.source

import com.br.queroajudar.categories.CategoriesRemoteDataSource
import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class DefaultCategoriesRepository @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource
) {


    fun getCauses() = resultLiveData(
        networkCall = {remoteDataSource.fetchCauses()}
    )

    fun getSkills() = resultLiveData(
        networkCall = {remoteDataSource.fetchSkills()}
    )


//    suspend fun getCauses() : ResultWrapper<SuccessResponse<List<Category>>> {
//        return apiCaller.safeApiCall(Dispatchers.IO) { QueroAjudarApi.retrofitService.getCauses()}
//    }
//
//    suspend fun getSkills() : ResultWrapper<SuccessResponse<List<Category>>> {
//        return apiCaller.safeApiCall(Dispatchers.IO) { QueroAjudarApi.retrofitService.getSkills()}
//    }


}