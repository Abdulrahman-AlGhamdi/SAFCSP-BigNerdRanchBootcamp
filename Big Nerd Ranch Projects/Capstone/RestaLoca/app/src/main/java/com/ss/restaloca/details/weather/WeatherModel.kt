package com.ss.restaloca.details.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
) : Parcelable

@Parcelize
data class Current(
    val condition: Condition,
    val is_day: Int,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double
) : Parcelable

@Parcelize
data class Forecast(
    val forecastday: List<Forecastday>
) : Parcelable

@Parcelize
data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
) : Parcelable

@Parcelize
data class Condition(
    val code: Int,
    val icon: String,
    val text: String
) : Parcelable

@Parcelize
data class Forecastday(
    val date: String,
    val day: Day,
    val hour: List<Hour>
) : Parcelable

@Parcelize
data class Day(
    val condition: Condition
) : Parcelable

@Parcelize
data class Hour(
    val chance_of_rain: String,
    val chance_of_snow: String,
    val condition: Condition,
    val is_day: Int,
    val temp_c: Double,
    val temp_f: Double,
    val time: String,
) : Parcelable