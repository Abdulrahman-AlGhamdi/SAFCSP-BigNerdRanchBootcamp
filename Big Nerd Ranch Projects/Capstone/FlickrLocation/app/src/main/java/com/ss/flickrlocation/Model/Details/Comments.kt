package com.ss.flickrlocation.Model.Details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comments(
    val _content: String
) : Parcelable