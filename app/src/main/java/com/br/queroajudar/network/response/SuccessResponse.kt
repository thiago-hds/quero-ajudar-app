package com.br.queroajudar.network.response

data class SuccessResponse<T>(
    val status : String,
    val message : String?,
    val data : T
)