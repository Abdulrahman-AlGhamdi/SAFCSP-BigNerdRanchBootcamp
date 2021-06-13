package com.ss.techmarket.common

import com.google.gson.annotations.SerializedName

data class City(
    val id: Int,
    val region: Int,
    @SerializedName("name_ar") val nameAR: String,
    @SerializedName("name_en") val nameEN: String
)

data class CityResponse(
    val data: List<City>
)

data class Region(
    val id: Int,
    @SerializedName("capital_city") val capitalCity: Int,
    val code: String,
    @SerializedName("name_ar") val nameAR: String,
    @SerializedName("name_en") val nameEN: String,
    val population: Int
)

data class RegionResponse(
    val data: List<Region>
)