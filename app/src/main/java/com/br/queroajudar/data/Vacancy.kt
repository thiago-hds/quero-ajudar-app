package com.br.queroajudar.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Vacancy(
    val id : Int,
    val name : String,
    val description : String,
    val type : String,
    val tasks : String,
    val image : String?,
    val organization: Organization,
    val address : Address

)