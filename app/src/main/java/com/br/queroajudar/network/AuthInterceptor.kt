package com.br.queroajudar.network

import android.content.Context
import com.br.queroajudar.util.QueroAjudarPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Accept", "application/json")
        // If token has been saved, add it to the request
        QueroAjudarPreferences.apiToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }

}