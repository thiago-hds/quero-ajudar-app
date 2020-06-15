package com.br.queroajudar.data.source

import com.br.queroajudar.util.VacancyPagedListing
import com.br.queroajudar.util.resultLiveData
import kotlinx.coroutines.CoroutineScope

interface VacanciesRepository {
    fun getPagedVacancies(
        coroutineScope: CoroutineScope
    ): VacancyPagedListing
}