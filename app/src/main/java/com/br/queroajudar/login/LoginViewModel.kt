package com.br.queroajudar.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.br.queroajudar.data.User
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.data.formdata.LoginData
import com.br.queroajudar.data.source.DefaultCategoriesRepository
import com.br.queroajudar.data.source.DefaultOrganizationsRepository
import com.br.queroajudar.data.source.DefaultVacanciesRepository
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.register.UserRepository
import com.br.queroajudar.util.QueroAjudarPreferences
import kotlinx.coroutines.*
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginData : LoginData,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){

    fun getLogin(): LoginData? {
        return loginData
    }

    fun doLogin(): LiveData<ResultWrapper<User>> {
        return userRepository.postLogin(loginData)
    }

    fun showErrors(errorsMap : Map<String,List<String>>){
        loginData.setApiValidationErrors(errorsMap)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}