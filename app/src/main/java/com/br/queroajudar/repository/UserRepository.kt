package com.br.queroajudar.repository

import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.model.User
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.ResultWrapper
import kotlinx.coroutines.*


class UserRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val  apiCaller : SafeApiCaller =
        SafeApiCaller()

    suspend fun postLogin(loginInfo : LoginInfo) : ResultWrapper<SuccessResponse<Map<String, User>>> {
        return apiCaller.safeApiCall(dispatcher) { QueroAjudarApi.retrofitService.postLogin(loginInfo)}
    }
}