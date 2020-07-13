package com.br.queroajudar.register

import com.br.queroajudar.data.User
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.formdata.LoginData
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.network.ApiService
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val service: ApiService,
    private val apiCaller: SafeApiCaller
) {

    suspend fun register(data: RegisterData): ResultWrapper<User> {
        return apiCaller.safeApiCall(Dispatchers.IO) {
            service.postRegister(data)
        }
    }

    suspend fun login(data: LoginData): ResultWrapper<User> {
        return apiCaller.safeApiCall(Dispatchers.IO) {
            service.postLogin(data)
        }
    }
}