package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.formdata.RegisterData
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.UserRepository
import com.br.queroajudar.util.QueroAjudarPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class RegisterViewModel : ViewModel() {
    private val registerData : RegisterData = RegisterData()
    private val userRepository : UserRepository = UserRepository()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    private val _apiStatus = MutableLiveData<ApiStatus>()
//    val apiStatus: LiveData<ApiStatus>
//        get() = _apiStatus

    fun getRegister(): RegisterData? {
        return registerData
    }

    fun onButtonSendClick() {
        //TODO validação dos campos no lado do cliente
//        register()
    }

//    private fun register(){
//        _apiStatus.value = ApiStatus.LOADING
//        coroutineScope.launch {
//            when (val loginResponse = userRepository.postRegister(registerData)) {
//                is ResultWrapper.NetworkError -> onNetworkError()
//                is ResultWrapper.GenericError -> onGenericError(loginResponse)
//                is ResultWrapper.Success -> onSuccess(loginResponse.value)
//            }
//        }
//    }

//    private fun onNetworkError( ){
//        Timber.i("API call network error")
//        _apiStatus.value = ApiStatus.NETWORK_ERROR
//    }
//
//    private fun onGenericError(loginResponse: ResultWrapper.GenericError) {
//        Timber.i("API call generic error: $loginResponse")
//        _apiStatus.value = ApiStatus.GENERIC_ERROR
//        if(loginResponse.error != null) {
//            registerData.setApiValidationErrors(loginResponse.error.errors)
//        }
//    }
//
//    private fun onSuccess(value: SuccessResponse<String>) {
//        Timber.i("API call success: $value")
//        QueroAjudarPreferences.apiToken = value.data
//        _apiStatus.value = ApiStatus.DONE
//    }

}