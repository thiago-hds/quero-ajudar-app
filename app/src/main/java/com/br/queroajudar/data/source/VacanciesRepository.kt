package com.br.queroajudar.data.source

import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.util.ItemPagedListing
import kotlinx.coroutines.CoroutineScope

interface VacanciesRepository {
    fun getPagedVacancies(
        coroutineScope: CoroutineScope,
        organizationId: Int? = null
    ): ItemPagedListing<Vacancy>
}