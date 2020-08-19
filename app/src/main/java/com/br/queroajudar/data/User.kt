package com.br.queroajudar.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id : Int,
    val first_name : String,
    val last_name : String,
    val email : String,
    @Json(name="date_of_birth") val dateOfBirth : String,
    val status : Int,
    val token : String?
)