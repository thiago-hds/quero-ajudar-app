package com.br.queroajudar.model

import com.squareup.moshi.Json

data class User(
    val id : Int,
    val name : String,
    val email : String,
    @Json(name="date_of_birth") val dateOfBirth : String,
    val status : String,
    val token : String

)