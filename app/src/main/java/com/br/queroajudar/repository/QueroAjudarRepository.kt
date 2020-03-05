package com.br.queroajudar.repository

import com.br.queroajudar.model.Cause
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.model.formdata.RegisterData
import com.br.queroajudar.network.QueroAjudarApi
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class QueroAjudarRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()

    suspend fun getCauses() : ResultWrapper<SuccessResponse<List<Cause>>> {
        return apiCaller.safeApiCall(dispatcher) { QueroAjudarApi.retrofitService.getCauses()}
    }
}