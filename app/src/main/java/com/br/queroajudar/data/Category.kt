package com.br.queroajudar.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Category(
    val id : Int,
    val name : String,
    @Json(name="fontawesome_icon_unicode") var iconUnicode : String?,
    var selected : Boolean = false
): Parcelable