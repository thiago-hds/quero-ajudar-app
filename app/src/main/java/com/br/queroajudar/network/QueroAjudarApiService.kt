package com.br.queroajudar.network

import android.util.Log
import com.br.queroajudar.model.BasicResponse
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.model.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.1.109:8000/api/"

val logging = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.i("QueroAjudar.APICall", message)
    }
}).setLevel(HttpLoggingInterceptor.Level.BODY)

val okHttpClientBuilder = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(logging)

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClientBuilder.build())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))

    .build()

interface QueroAjudarApiService{

    @POST("login")
    fun login(@Body loginInfo: LoginInfo): Deferred<Response<BasicResponse<Map<String, User>>>>

}

object QueroAjudarApi{
    val retrofitService : QueroAjudarApiService by lazy{
        retrofit.create(QueroAjudarApiService::class.java)
    }
}