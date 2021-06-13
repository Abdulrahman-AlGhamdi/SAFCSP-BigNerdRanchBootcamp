package com.ss.flickrlocation.Model.Details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dates(
    val lastupdate: String,
    val taken: String
) : Parcelable