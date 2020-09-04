package com.br.queroajudar.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Organization(
    val id : Int,
    val name : String,
    val logo : String?,
    val description : String,
    val phones: List<String>,
    val website: String,
    val email: String,
    val causes: List<Category>,
    @Json(name="formatted_location") val formattedLocation: String?,
    val favorite: Boolean
)