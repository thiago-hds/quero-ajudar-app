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
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){


    private val loginData : LoginData = LoginData()


    fun getLogin(): LoginData? {
        return loginData
    }

    fun onButtonEnterClick() {
        Log.i("QueroAjudar", "Clicou")
        if (loginData.isValid()) {
            Log.i("QueroAjudar", "Login is valid")
            doLogin()
        }
        else{
            Log.i("QueroAjudar", "Login is not valid")
        }
    }


    fun doLogin(): LiveData<ResultWrapper<User>> {
        return userRepository.postLogin(loginData)
    }

//    private fun showNetworkError( ){
//        Log.i("QueroAjudar", "Network Error")
//        _apiStatus.value = ApiStatus.NETWORK_ERROR
//    }
//
//    private fun showGenericError(loginResponse: ResultWrapper.GenericError) {
//        Log.i("QueroAjudar", "Error! $loginResponse")
//        _apiStatus.value = ApiStatus.GENERIC_ERROR
//        if(loginResponse.error != null) {
//            loginData.setApiValidationErrors(loginResponse.error.errors)
//        }
//    }

    fun saveToken(token: String){
        QueroAjudarPreferences.apiToken = token
    }

    fun showErrors(errorsMap : Map<String,List<String>>){
        loginData.setApiValidationErrors(errorsMap)
    }
//
//    private fun showSuccess(value: SuccessResponse<String>) {
//        Log.i("QueroAjudar", "Success! $value")
//        _apiStatus.value = ApiStatus.DONE
//    }


    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}