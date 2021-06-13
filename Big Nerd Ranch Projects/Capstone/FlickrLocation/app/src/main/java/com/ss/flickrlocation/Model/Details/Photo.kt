package com.ss.flickrlocation.Model.Details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val comments: Comments,
    val dates: Dates,
    val dateuploaded: String,
    val id: String,
    val location: Location,
    val owner: Owner,
    val secret: String,
    val server: String,
    val views: String
) : Parcelable