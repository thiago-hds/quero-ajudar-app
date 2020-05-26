package com.br.queroajudar.categories

import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource
) {


    val causes = resultLiveData(
        networkCall = {remoteDataSource.fetchCauses()}
    )

    val skills = resultLiveData(
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