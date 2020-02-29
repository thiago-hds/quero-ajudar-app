package com.br.queroajudar.network

import android.util.Log
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.LoginInfo
import com.br.queroajudar.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.1.109:8000/api/"



val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
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
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface QueroAjudarApiService {

    @POST("login")
    suspend fun postLogin(@Body loginInfo: LoginInfo): SuccessResponse<Map<String, User>>
}

object QueroAjudarApi {
    val retrofitService: QueroAjudarApiService by lazy {
        retrofit.create(QueroAjudarApiService::class.java)
    }
}

enum class QueroAjudarApiStatus { LOADING, ERROR, DONE }