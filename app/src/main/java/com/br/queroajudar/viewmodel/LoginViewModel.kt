package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.model.User
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.repository.UserRepository
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*


class LoginViewModel : ViewModel() {
    private val loginInfo : LoginInfo = LoginInfo()


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val userRepository : UserRepository = UserRepository()

    private val _apiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val apiStatus: LiveData<QueroAjudarApiStatus>
        get() = _apiStatus


    fun getLogin(): LoginInfo? {
        return loginInfo
    }

    fun onButtonEnterClick() {
        Log.i("QueroAjudar", "Clicou")
        //if (loginInfo.isValid()) {
            Log.i("QueroAjudar", "Login is valid")
            doLogin()
        //}
//        else{
//            Log.i("QueroAjudar", "Login is not valid")
//        }
    }

    @BindingAdapter("error")
    fun setError(textLayout: TextInputLayout, strOrResId: Any?) {
        if (strOrResId is Int) {
            textLayout.error = textLayout.context.getString((strOrResId as Int?)!!)
        } else {
            textLayout.error = strOrResId as String?
        }
    }

    private fun doLogin(){
        coroutineScope.launch {
            val loginResponse = userRepository.postLogin(loginInfo)
            _apiStatus.value = QueroAjudarApiStatus.LOADING
            when (loginResponse) {
                is ResultWrapper.NetworkError -> showNetworkError()
                is ResultWrapper.GenericError -> showGenericError(loginResponse)
                is ResultWrapper.Success -> showSuccess(loginResponse.value)
            }

        }
    }

    fun showNetworkError( ){
        Log.i("QueroAjudar", "Network Error")
        _apiStatus.value = QueroAjudarApiStatus.ERROR
    }

    fun showGenericError(loginResponse: ResultWrapper.GenericError) {
        Log.i("QueroAjudar", "Error! $loginResponse")
        _apiStatus.value = QueroAjudarApiStatus.ERROR
    }

    fun showSuccess(value: SuccessResponse<Map<String, User>>) {
        Log.i("QueroAjudar", "Success! $value")
        _apiStatus.value = QueroAjudarApiStatus.DONE
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}