package com.br.queroajudar.network

import android.util.Log
import com.br.queroajudar.model.Cause
import com.br.queroajudar.network.response.SuccessResponse
import com.br.queroajudar.model.formdata.LoginData
import com.br.queroajudar.model.formdata.RegisterData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.1.109:8000/api/"

val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.i("QueroAjudar.APICall", message)
    }
}).setLevel(HttpLoggingInterceptor.Level.BODY)


val okHttpClientBuilder = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .authenticator(TokenAuthenticator())
    .addInterceptor(loggingInterceptor)


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
    suspend fun postLogin(@Body data: LoginData): SuccessResponse<String>

    @POST("register")
    suspend fun postRegister(@Body data: RegisterData): SuccessResponse<String>

    @GET("causes")
    suspend fun getCauses(): SuccessResponse<List<Cause>>
}

object QueroAjudarApi {
    val retrofitService: QueroAjudarApiService by lazy {
        retrofit.create(QueroAjudarApiService::class.java)
    }
}

enum class QueroAjudarApiStatus { LOADING, DONE, GENERIC_ERROR, NETWORK_ERROR }