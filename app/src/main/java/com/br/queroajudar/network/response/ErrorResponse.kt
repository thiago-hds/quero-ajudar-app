package com.br.queroajudar.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val status : String,
    val message : String?,
    val errors: Map<String,List<String>>
)