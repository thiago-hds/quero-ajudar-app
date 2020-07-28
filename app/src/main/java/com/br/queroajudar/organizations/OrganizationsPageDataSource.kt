package com.br.queroajudar.organizations

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.data.Organization
import com.br.queroajudar.network.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class OrganizationsPageDataSource @Inject constructor(
    private val scope: CoroutineScope,
    private val remoteDataSource: OrganizationsRemoteDataSource,
    private val causes:List<Int>,
    private val skills:List<Int>
) : PageKeyedDataSource<Int, Organization>() {

    val loadInitialResultWrapper = MutableLiveData<ResultWrapper<Any>>()
    val loadAfterResultWrapper = MutableLiveData<ResultWrapper<Any>>()
    val organizationsSize = MutableLiveData<Int>()

    var getFavorites = false


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Organization>
    ) {
        fetchData(1, causes, skills, loadInitialResultWrapper){
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Organization>) {
        fetchData(params.key, causes, skills, loadAfterResultWrapper){
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Organization>) {}

    private fun fetchData(page : Int = 1, causes:List<Int> = listOf(), skills:List<Int> = listOf(),
                          resultWrapperLiveData: MutableLiveData<ResultWrapper<Any>>,
                          callback: (List<Organization>) -> Unit) {

        scope.launch {
            resultWrapperLiveData.postValue(ResultWrapper.Loading)
            val getOrganizationsResponse = if (getFavorites){
                remoteDataSource.fetchFavoriteOrganizations(page)
            }
            else {
                remoteDataSource.fetchOrganizations(page, causes, skills)
            }
            resultWrapperLiveData.postValue(getOrganizationsResponse)

            Timber.i("organizations fetched %s", getOrganizationsResponse)

            if(getOrganizationsResponse is ResultWrapper.Success){
                val organizations = getOrganizationsResponse.value
                organizationsSize.postValue(organizationsSize.value?.plus((organizations.size)) ?: 0)
                callback(organizations)
            }
        }
    }
}