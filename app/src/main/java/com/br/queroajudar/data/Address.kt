package com.br.queroajudar.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    val id : Int,
    val location : String
)