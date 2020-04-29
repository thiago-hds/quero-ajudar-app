package com.br.queroajudar.util

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.br.queroajudar.network.QueroAjudarApiStatus

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class PagedListing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    val loadInitialApiStatus: LiveData<QueroAjudarApiStatus>,
    val loadAfterApiStatus: LiveData<QueroAjudarApiStatus>,
    val refresh: () -> Unit
)

data class Listing<T>(
    val list: LiveData<List<T>>,
    val apiStatus: LiveData<QueroAjudarApiStatus>
)