package com.br.queroajudar.register

import com.br.queroajudar.network.SafeApiCaller
//import com.br.queroajudar.network.QueroAjudarApi
import kotlinx.coroutines.*


class UserRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()

//    suspend fun postLogin(data : LoginData) : ResultWrapper<SuccessResponse<String>> {
//        return apiCaller.safeApiCall(dispatcher) { QueroAjudarApi.retrofitService.postLogin(data)}
//    }
//
//    suspend fun postRegister(data : RegisterData) : ResultWrapper<SuccessResponse<String>> {
//        return apiCaller.safeApiCall(dispatcher) { QueroAjudarApi.retrofitService.postRegister(data)}
//    }
}