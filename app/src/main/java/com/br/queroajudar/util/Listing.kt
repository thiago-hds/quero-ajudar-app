package com.br.queroajudar.util

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.network.ResultWrapper

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class VacancyPagedListing(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<Vacancy>>,
    val size : LiveData<Int>,
    val loadInitialResultWrapper: LiveData<ResultWrapper<Any>>,
    val loadAfterResultWrapper: LiveData<ResultWrapper<Any>>,
    val refresh: (List<Int>, List<Int>, Int?) -> Unit
)