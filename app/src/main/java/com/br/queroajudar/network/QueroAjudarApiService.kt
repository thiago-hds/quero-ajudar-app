package com.br.queroajudar.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://localhost:8000/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface QueroAjudarApiService{
    @GET("test")
    fun getProperties(): Call<String>
}

object QueroAjudarApi{
    val retrofitService : QueroAjudarApiService by lazy{
        retrofit.create(QueroAjudarApiService::class.java)
    }
}