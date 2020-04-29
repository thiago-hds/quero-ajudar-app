package com.br.queroajudar.repository.datasource

import androidx.paging.PageKeyedDataSource
import com.br.queroajudar.model.Vacancy
import kotlinx.coroutines.CoroutineScope

//class VacancyDataSource(private val scope: CoroutineScope,
//                        private val causes:String,
//                        private val skills:String
//) : DataSource<Int, Vacancy>() {