package com.br.queroajudar.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.model.Category
import com.br.queroajudar.model.Organization
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.SafeApiCaller
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.datasource.OrganizationRemoteDataSource
import com.br.queroajudar.util.resultLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val remoteDataSource: OrganizationRemoteDataSource
){

    //val organizations = remoteDataSource.fetchOrganizations(1)

    val organizations = resultLiveData(
        networkCall = {remoteDataSource.fetchOrganizations((1))}
    )

}