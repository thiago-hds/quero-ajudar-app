package com.br.queroajudar.causes

import androidx.lifecycle.ViewModel
//import com.br.queroajudar.network.ApiStatus
import kotlinx.coroutines.Job

class CausesViewModel : ViewModel() {
    private var viewModelJob = Job()
//    private val repository : CategoryRepository = CategoryRepository()
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    private val _apiStatus = MutableLiveData<ApiStatus>()
//    val apiStatus: LiveData<ApiStatus>
//        get() = _apiStatus

//    private fun getCauses(){
//        coroutineScope.launch {
//            when (val getCausesResponse = repository.getCauses()) {
//                is ResultWrapper.NetworkError -> onNetworkError()
//                is ResultWrapper.GenericError -> onGenericError(getCausesResponse)
//                is ResultWrapper.Success -> onSuccess(getCausesResponse.value)
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
//    }
//
//    private fun onSuccess(value: SuccessResponse<List<Category>>) {
//        Timber.i("API call success: $value")
//        _apiStatus.value = ApiStatus.DONE
//    }

}