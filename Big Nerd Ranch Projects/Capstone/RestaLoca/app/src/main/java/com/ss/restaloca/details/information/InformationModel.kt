package com.ss.restaloca.details.information

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InformationModel(
    val alias: String,
    val categories: List<Category>,
    val coordinates: Coordinates,
    val display_phone: String?,
    val hours: List<Hour>?,
    val id: String,
    val image_url: String,
    val is_claimed: Boolean,
    val is_closed: Boolean,
    val location: Location,
    val name: String,
    val phone: String?,
    val photos: List<String>,
    val price: String?,
    val rating: Double,
    val review_count: Int,
    val url: String
) : Parcelable

@Parcelize
data class Category(
    val alias: String,
    val title: String
) : Parcelable

@Parcelize
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) : Parcelable

@Parcelize
data class Hour(
    val hours_type: String,
    val is_open_now: Boolean,
    val open: List<Open>
) : Parcelable

@Parcelize
data class Location(
    val address1: String,
    val address2: String,
    val address3: String,
    val city: String,
    val country: String,
    val cross_streets: String,
    val display_address: List<String>,
    val state: String,
    val zip_code: String
) : Parcelable

@Parcelize
data class Open(
    val day: Int,
    val end: String,
    val is_overnight: Boolean,
    val start: String
) : Parcelable