package com.ss.flickrlocation.Model.Image

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class Photo(
    @PrimaryKey
    var id: String,
    var datetaken: String,
    var owner: String,
    var secret: String,
    var server: String,
    var latitude: String,
    var longitude: String,
    var added: Boolean = false
) : Parcelable