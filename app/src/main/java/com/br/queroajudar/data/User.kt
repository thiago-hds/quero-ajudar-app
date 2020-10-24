package com.br.queroajudar.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    val id : Int,
    val first_name : String,
    val last_name : String,
    val email : String,
    @Json(name="date_of_birth") val dateOfBirth : String,
    val status : Int,
    val token : String?,
    @Json(name="applications_count") val applicationsCount: Int,
    val causes: List<Category>?,
    val skills: List<Category>?
): Parcelable