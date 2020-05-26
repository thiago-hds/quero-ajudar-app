package com.br.queroajudar.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Organization(
    val id : Int,
    val name : String,
    val logo : String
)