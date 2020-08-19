package com.br.queroajudar.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.User
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.QueroAjudarPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){


    val registerData : RegisterData = RegisterData()
    lateinit var user: LiveData<ResultWrapper<User>>


//    private val _apiStatus = MutableLiveData<ApiStatus>()
//    val apiStatus: LiveData<ApiStatus>
//        get() = _apiStatus

//    fun getRegister(): RegisterData? {
//        return registerData
//    }

    fun register(): LiveData<ResultWrapper<User>> {
        return userRepository.postRegister(registerData)
    }

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

    fun saveApiToken(token: String){
        QueroAjudarPreferences.apiToken = token
    }

}