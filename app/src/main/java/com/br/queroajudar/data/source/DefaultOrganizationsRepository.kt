package com.br.queroajudar.data.source

import com.br.queroajudar.organizations.OrganizationsRemoteDataSource
import com.br.queroajudar.util.resultLiveData
import javax.inject.Inject

class DefaultOrganizationsRepository @Inject constructor(
    private val remoteDataSource: OrganizationsRemoteDataSource
){

    fun getOrganizations() = resultLiveData(
        networkCall = {remoteDataSource.fetchOrganizations((1))}
    )

    fun getOrganization(id: Int) = resultLiveData(
        networkCall = {remoteDataSource.fetchOrganization(id)}
    )

    fun favoriteOrganization(id: Int) = resultLiveData(
        networkCall = {remoteDataSource.favoriteOrganization(id)}
    )

}