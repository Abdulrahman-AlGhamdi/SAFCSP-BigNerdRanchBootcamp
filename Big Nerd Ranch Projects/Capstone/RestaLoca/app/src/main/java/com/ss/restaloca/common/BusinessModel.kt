package com.ss.restaloca.common

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ss.restaloca.common.Constants.RECENT_SEARCH_TABLE_NAME
import kotlinx.parcelize.Parcelize

data class BusinessModel(
    val businesses: List<Businesse>?,
    val region: Region?,
    val total: Int?
)

@Parcelize
@Entity(tableName = RECENT_SEARCH_TABLE_NAME)
data class Businesse(
    @PrimaryKey
    val id: String,
    val categories: List<Category>,
    val coordinates: Coordinates,
    val display_phone: String,
    val distance: Double,
    val image_url: String,
    val is_closed: Boolean,
    val location: Location,
    val price: String?,
    val name: String,
    val phone: String,
    val rating: Double,
    val review_count: Int,
    val url: String
) : Parcelable

data class Region(
    val center: Center?
)

@Parcelize
data class Category(
    val title: String
) : Parcelable

@Parcelize
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) : Parcelable

@Parcelize
data class Location(
    val address1: String,
    val city: String,
) : Parcelable

data class Center(
    val latitude: Double,
    val longitude: Double
)