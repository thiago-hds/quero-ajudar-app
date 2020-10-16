package com.br.queroajudar.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.br.queroajudar.data.User
import com.br.queroajudar.data.formdata.RegisterData
import com.br.queroajudar.di.CoroutineScopeIO
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.REGISTER_MODE
import com.br.queroajudar.util.QueroAjudarPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    val registerData: RegisterData,
    @CoroutineScopeIO private val coroutineScope: CoroutineScope
) : ViewModel(){

    lateinit var user: LiveData<ResultWrapper<User>>

    var mode = REGISTER_MODE

    fun sendData(): LiveData<ResultWrapper<User>> {
        return if(mode == REGISTER_MODE){
            register()
        } else{
            editProfile()
        }
    }

    private fun register(): LiveData<ResultWrapper<User>> {
        return userRepository.postRegister(registerData)
    }

    private fun editProfile(): LiveData<ResultWrapper<User>> {
        return userRepository.postEditProfile(registerData)
    }

    fun setUserData(user: User){
        registerData.firstName = user.first_name
        registerData.lastName = user.last_name
        registerData.email = user.email
        registerData.password = null
    }

}