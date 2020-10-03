package com.br.queroajudar.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Address(
    val id : Int,
    val location : String
): Parcelable