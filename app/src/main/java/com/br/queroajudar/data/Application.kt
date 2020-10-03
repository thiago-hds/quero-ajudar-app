package com.br.queroajudar.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Application(
    val id : Int,
    @Json(name="volunteer_user_id")
    val volunteerUserId : String,
    @Json(name="vacancy_id")
    val vacancyId : String,
    val status: Int
) : Parcelable