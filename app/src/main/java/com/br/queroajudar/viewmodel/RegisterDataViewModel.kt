package com.br.queroajudar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.br.queroajudar.network.QueroAjudarApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterDataViewModel : ViewModel() {
     fun test(){
        QueroAjudarApi.retrofitService.getProperties().enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("QueroAjudar", "Deu ruim")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("QueroAjudar", response.body())
            }

        })
    }
}