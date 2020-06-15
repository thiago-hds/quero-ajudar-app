package com.br.queroajudar.vacancies

import com.br.queroajudar.data.source.DefaultCategoriesRepository
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class VacanciesViewModelTest {

    private lateinit var viewModel: VacanciesViewModel

    @Before
    fun setUp() {

        val vacanciesRepository = mock(DefaultVacanciesRepository::class.java)
        val categoriesRepository = mock(DefaultCategoriesRepository::class.java)
        val organizationsRepository = mock(DefaultOrganizationsRepository::class.java)


        viewModel = VacanciesViewModel(
            vacanciesRepository,
            categoriesRepository,
            organizationsRepository, CoroutineScope(Dispatchers.Unconfined))


    }

    @Test
    fun loadVacancies_loadingInitialTogglesAndDataLoaded(){
        viewModel.loadVacancies()
    }



}