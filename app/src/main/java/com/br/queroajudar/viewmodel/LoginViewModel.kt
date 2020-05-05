package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.network.ApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.repository.UserRepository
import kotlinx.coroutines.*


class LoginViewModel : ViewModel() {
    private val loginData : LoginData = LoginData()
    private val userRepository : UserRepository = UserRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus


    fun getLogin(): LoginData? {
        return loginData
    }

    fun onButtonEnterClick() {
        Log.i("QueroAjudar", "Clicou")
        //if (loginData.isValid()) {
            Log.i("QueroAjudar", "Login is valid")
//            doLogin()
        //}
//        else{
//            Log.i("QueroAjudar", "Login is not valid")
//        }
    }


//    private fun doLogin(){
//        _apiStatus.value = ApiStatus.LOADING
//        coroutineScope.launch {
//            when (val loginResponse = userRepository.postLogin(loginData)) {
//                is ResultWrapper.NetworkError -> showNetworkError()
//                is ResultWrapper.GenericError -> showGenericError(loginResponse)
//                is ResultWrapper.Success -> showSuccess(loginResponse.value)
//            }
//        }
//    }

    private fun showNetworkError( ){
        Log.i("QueroAjudar", "Network Error")
        _apiStatus.value = ApiStatus.NETWORK_ERROR
    }

    private fun showGenericError(loginResponse: ResultWrapper.GenericError) {
        Log.i("QueroAjudar", "Error! $loginResponse")
        _apiStatus.value = ApiStatus.GENERIC_ERROR
        if(loginResponse.error != null) {
            loginData.setApiValidationErrors(loginResponse.error.errors)
        }
    }

    private fun showSuccess(value: SuccessResponse<String>) {
        Log.i("QueroAjudar", "Success! $value")
        _apiStatus.value = ApiStatus.DONE
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}