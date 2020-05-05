package com.br.queroajudar.repository

import com.br.queroajudar.model.Category
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.model.formdata.RegisterData
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.datasource.CategoryRemoteDataSource
import com.br.queroajudar.util.resultLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource
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