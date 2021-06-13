package com.ss.flickrlocation.Model.Details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val accuracy: String,
    val context: String,
    val country: Country,
    val latitude: String,
    val locality: Locality,
    val longitude: String,
    val region: Region
) : Parcelable