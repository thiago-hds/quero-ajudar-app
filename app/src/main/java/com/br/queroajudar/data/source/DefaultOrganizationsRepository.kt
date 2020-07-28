package com.br.queroajudar.data.source

import android.provider.ContactsContract
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.br.queroajudar.data.Organization
import com.br.queroajudar.organizations.OrganizationsPageDataSourceFactory
import com.br.queroajudar.organizations.OrganizationsRemoteDataSource
import com.br.queroajudar.util.ItemPagedListing
import com.br.queroajudar.util.resultLiveData
import kotlinx.coroutines.CoroutineScope
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

    fun getPagedFavoriteOrganizations(coroutineScope: CoroutineScope): ItemPagedListing<Organization> {
        val dataSourceFactory =
            OrganizationsPageDataSourceFactory(
                coroutineScope,
                remoteDataSource,
                getFavorites = true
            )

        val livePagedList = LivePagedListBuilder(dataSourceFactory,
            OrganizationsPageDataSourceFactory.pagedListConfig()
        ).build()

        return ItemPagedListing<Organization>(
            pagedList = livePagedList,
            loadInitialResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadInitialResultWrapper
            },
            loadAfterResultWrapper = Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    dataSource -> dataSource.loadAfterResultWrapper
            },
            size = Transformations.switchMap(dataSourceFactory.mutableLiveData){
                    dataSource -> dataSource.organizationsSize
            },
            refresh = { causesIds: List<Int>?, skillsIds: List<Int>?, organizationId: Int? ->
                dataSourceFactory.mutableLiveData.value?.invalidate()
            }
        )
    }

}