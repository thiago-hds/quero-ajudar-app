package com.br.queroajudar.profile

import com.br.queroajudar.data.formdata.LoginData
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.register.UserRemoteDataSource
import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource
) {

    fun getProfile() = resultLiveData(
        networkCall = {remoteDataSource.fetchProfile()}
    )
}