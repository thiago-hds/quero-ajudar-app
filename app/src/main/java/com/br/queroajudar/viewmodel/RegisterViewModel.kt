package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.User
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.model.formdata.RegisterData
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val registerData : RegisterData = RegisterData()
    private val userRepository : UserRepository = UserRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _apiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val apiStatus: LiveData<QueroAjudarApiStatus>
        get() = _apiStatus

    fun getRegister(): RegisterData? {
        return registerData
    }

    fun onButtonSendClick() {
        Log.i("QueroAjudar", "Clicou")
        //if (loginData.isValid()) {
        Log.i("QueroAjudar", "Login is valid")
        register()
        //}
//        else{
//            Log.i("QueroAjudar", "Login is not valid")
//        }
    }

    private fun register(){
        _apiStatus.value = QueroAjudarApiStatus.LOADING
        coroutineScope.launch {
            when (val loginResponse = userRepository.postRegister(registerData)) {
                is ResultWrapper.NetworkError -> showNetworkError()
                is ResultWrapper.GenericError -> showGenericError(loginResponse)
                is ResultWrapper.Success -> showSuccess(loginResponse.value)
            }
        }
    }

    private fun showNetworkError( ){
        Log.i("QueroAjudar", "Network Error")
        _apiStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
    }

    private fun showGenericError(loginResponse: ResultWrapper.GenericError) {
        Log.i("QueroAjudar", "Error! $loginResponse")
        _apiStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
        if(loginResponse.error != null) {
            registerData.setApiValidationErrors(loginResponse.error.errors)
        }
    }

    private fun showSuccess(value: SuccessResponse<Map<String, User>>) {
        Log.i("QueroAjudar", "Success! $value")
        _apiStatus.value = QueroAjudarApiStatus.DONE
    }

}