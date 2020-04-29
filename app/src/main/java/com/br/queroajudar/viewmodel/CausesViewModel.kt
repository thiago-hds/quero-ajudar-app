package com.br.queroajudar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.Category
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.repository.QueroAjudarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class CausesViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val repository : QueroAjudarRepository = QueroAjudarRepository()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _apiStatus = MutableLiveData<QueroAjudarApiStatus>()
    val apiStatus: LiveData<QueroAjudarApiStatus>
        get() = _apiStatus

    private fun getCauses(){
        coroutineScope.launch {
            when (val getCausesResponse = repository.getCauses()) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError(getCausesResponse)
                is ResultWrapper.Success -> onSuccess(getCausesResponse.value)
            }
        }
    }

    private fun onNetworkError( ){
        Timber.i("API call network error")
        _apiStatus.value = QueroAjudarApiStatus.NETWORK_ERROR
    }

    private fun onGenericError(loginResponse: ResultWrapper.GenericError) {
        Timber.i("API call generic error: $loginResponse")
        _apiStatus.value = QueroAjudarApiStatus.GENERIC_ERROR
    }

    private fun onSuccess(value: SuccessResponse<List<Category>>) {
        Timber.i("API call success: $value")
        _apiStatus.value = QueroAjudarApiStatus.DONE
    }

}
