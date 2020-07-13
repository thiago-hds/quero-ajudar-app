package com.br.queroajudar.register

import com.br.queroajudar.data.User
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.formdata.LoginData
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.util.resultLiveData
import com.br.queroajudar.vacancies.VacanciesRemoteDataSource
//import com.br.queroajudar.network.QueroAjudarApi
import kotlinx.coroutines.*
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {

    private val  apiCaller : SafeApiCaller = SafeApiCaller()

    fun postRegister(data : RegisterData) = resultLiveData(
        networkCall = {remoteDataSource.register(data)}
    )

    fun postLogin(data : LoginData) = resultLiveData(
        networkCall = {remoteDataSource.login(data)}
    )
}