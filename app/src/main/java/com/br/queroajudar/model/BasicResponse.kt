package com.br.queroajudar.model

import com.squareup.moshi.Json

data class BasicResponse<T>(
    val status : String,
    val message : String?,
    val data : T?
)