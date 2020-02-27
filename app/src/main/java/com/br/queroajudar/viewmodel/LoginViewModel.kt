package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.network.QueroAjudarApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    private val loginInfo : LoginInfo = LoginInfo()


    fun getLogin(): LoginInfo? {
        return loginInfo
    }

    fun onButtonEnterClick() {
        Log.i("QueroAjudar", "Clicou")
        if (loginInfo.isValid()) {
            Log.i("QueroAjudar", "Login is valid")
            doLogin()
        }
        else{
            Log.i("QueroAjudar", "Login is not valid")
        }
    }

    @BindingAdapter("error")
    fun setError(textLayout: TextInputLayout, strOrResId: Any?) {
        if (strOrResId is Int) {
            textLayout.error = textLayout.context.getString((strOrResId as Int?)!!)
        } else {
            textLayout.error = strOrResId as String?
        }
    }

    fun doLogin(){
        GlobalScope.launch(Dispatchers.Main){
            val webResponse  = QueroAjudarApi.retrofitService.login(loginInfo.email, loginInfo.password).await()
            Log.i("QueroAjudar", "Add success: ${webResponse.isSuccessful}")
        }

    }





}