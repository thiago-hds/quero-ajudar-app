package com.br.queroajudar.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@JsonClass(generateAdapter = true)
@Parcelize
data class Vacancy(
    val id : Int,
    val name : String,
    val description : String,
    val type : String,
    val tasks : String,
    val image : String?,
    val organization: Organization,
    val address : Address?,
    val causes: List<Category>,
    val skills: List<Category>,
    val periodicity: String?,
    @Json(name="amount_per_period") val amountPerPeriod: Int?,
    @Json(name="unit_per_period") val unitPerPeriod: String?,
    val date: String?,
    val time: String?,
    @Json(name="formatted_frequency") val formattedFrequency: String?,
    @Json(name="formatted_date") val formattedDate: String?,
    @Json(name="formatted_time") val formattedTime: String?,
    @Json(name="formatted_location") val formattedLocation: String?,
    val favorite: Boolean,
    val application: Application?
) : Parcelable