package com.ss.restaloca.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ss.restaloca.common.Category
import com.ss.restaloca.common.Coordinates
import com.ss.restaloca.common.Location

class RestaLocaConverters {

    @TypeConverter
    fun fromCategory(categories : List<Category>): String {
        return Gson().toJson(categories)
    }

    @TypeConverter
    fun toCategory(json: String) : List<Category> {
        return Gson().fromJson(json, Array<Category>::class.java).toList()
    }

    @TypeConverter
    fun fromCoordinates(coordinates: Coordinates): String {
        return Gson().toJson(coordinates)
    }

    @TypeConverter
    fun toCoordinates(json: String): Coordinates {
        return Gson().fromJson(json, Coordinates::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location) : String {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toLocation(json: String) : Location {
        return Gson().fromJson(json, Location::class.java)
    }
}