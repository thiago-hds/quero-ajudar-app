package com.br.queroajudar.organizationdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.network.ResultWrapper
import javax.inject.Inject
import kotlin.properties.Delegates

class OrganizationDetailsViewModel @Inject constructor(
    private val repository: DefaultOrganizationsRepository
) : ViewModel(){

    var id by Delegates.notNull<Int>()

    val organization by lazy { repository.getOrganization(id) }

    fun favoriteVacancy(): LiveData<ResultWrapper<Boolean>> {
        return repository.favoriteOrganization(id)
    }
}