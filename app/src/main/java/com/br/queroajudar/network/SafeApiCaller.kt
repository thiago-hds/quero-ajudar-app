package com.br.queroajudar.network

import android.util.Log
import com.br.queroajudar.network.response.ErrorResponse
import com.br.queroajudar.network.response.SuccessResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SafeApiCaller @Inject constructor(){
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> SuccessResponse<T>) : ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                var result = apiCall.invoke()
                ResultWrapper.Success(result.data)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        Log.i("HttpException", "Error: $throwable.message")
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> {
                        Log.i("HttpException", "Error: $throwable.message")
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            Log.i("QueroAjudar.erro", throwable.response()?.errorBody().toString())
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            Log.i("QueroAjudar.exception", exception.message ?: "null")
            null
        }
    }
}