package com.br.queroajudar.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id : Int,
    val name : String,
    @Json(name="fontawesome_icon_unicode") var iconUnicode : String?,
    var selected : Boolean = false
)