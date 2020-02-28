package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.ViewModel
import com.br.queroajudar.model.BasicResponse
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.model.User
import com.br.queroajudar.network.QueroAjudarApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    private val loginInfo : LoginInfo = LoginInfo()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


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
        coroutineScope.launch {
            var loginDeferred = QueroAjudarApi.retrofitService.login(loginInfo)
            try {
                var loginResponse = loginDeferred.await()
                if (loginResponse.status == "fail"){

                }
                Log.i("QueroAjudar","Success: ${loginResponse.data.toString()} Mars properties retrieved")
            }
            catch (t:Throwable){
                Log.i("QueroAjudar","Failure: " + t.message)

            }
        }

//        QueroAjudarApi.retrofitService.login(loginInfo).enqueue(object: Callback<BasicResponse<Map<String,User>>>{
//
//
//            override fun onFailure(call: Call<BasicResponse<Map<String, User>>>, t: Throwable) {
//                Log.i("QueroAjudar","Failure: " + t.message)
//            }
//
//            override fun onResponse(
//                call: Call<BasicResponse<Map<String, User>>>,
//                response: Response<BasicResponse<Map<String, User>>>
//            ) {
//                Log.i("QueroAjudar","Success: ${response.body()?.toString()} Mars properties retrieved")
//            }
//
//        })
//        GlobalScope.launch(Dispatchers.Main){
//            val webResponse  = QueroAjudarApi.retrofitService.login(loginInfo).await()
//            Log.i("QueroAjudar", "Add success: ${webResponse.isSuccessful}")
//        }



    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}