package com.br.queroajudar.viewmodel

import androidx.lifecycle.ViewModel
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.repository.QueroAjudarRepository
import com.br.queroajudar.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CausesViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val repository : QueroAjudarRepository = QueroAjudarRepository()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private fun getCauses(){
        coroutineScope.launch {
            when (val getCausesResponse = repository.getCauses()) {
                is ResultWrapper.NetworkError -> onNetworkError()
                is ResultWrapper.GenericError -> onGenericError()
                is ResultWrapper.Success -> onSuccess()
            }
        }
    }

    private fun onGenericError(loginResponse: Any) {

    }

    private fun onSuccess(loginResponse: Any) {

    }

    private fun onNetworkError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
}
