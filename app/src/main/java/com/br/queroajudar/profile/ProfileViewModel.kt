package com.br.queroajudar.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.source.DefaultCategoriesRepository
import com.br.queroajudar.network.ResultWrapper
import javax.inject.Inject
import kotlin.properties.Delegates

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val profile by lazy { profileRepository.getProfile() }
}