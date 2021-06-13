package com.ss.flickrlocation.Model.Details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    val realname: String,
    val username: String
) : Parcelable