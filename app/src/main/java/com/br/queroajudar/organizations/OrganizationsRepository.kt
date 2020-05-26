package com.br.queroajudar.organizations

import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class OrganizationsRepository @Inject constructor(
    private val remoteDataSource: OrganizationsRemoteDataSource
){

    //val organizations = remoteDataSource.fetchOrganizations(1)

    val organizations = resultLiveData(
        networkCall = {remoteDataSource.fetchOrganizations((1))}
    )

}