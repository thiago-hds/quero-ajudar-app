package com.br.queroajudar.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Organization(
    val id : Int,
    val name : String,
    val logo : String
)