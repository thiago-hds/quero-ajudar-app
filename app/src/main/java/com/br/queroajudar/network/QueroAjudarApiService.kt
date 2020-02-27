package com.br.queroajudar.network

import com.br.queroajudar.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.1.109:8000/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface QueroAjudarApiService{
    @GET("test")
    fun getProperties(): Call<String>

    @POST("login")
    //fun login() : Call<>
    fun login(@Field("email") email : String, @Field("password") password : String): Deferred<Response<Void>>

}

object QueroAjudarApi{
    val retrofitService : QueroAjudarApiService by lazy{
        retrofit.create(QueroAjudarApiService::class.java)
    }
}