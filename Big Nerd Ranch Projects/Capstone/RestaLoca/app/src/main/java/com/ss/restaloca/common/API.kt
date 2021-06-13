package com.ss.restaloca.common

import com.ss.restaloca.details.reviews.ReviewModel
import com.ss.restaloca.details.weather.WeatherModel
import com.ss.restaloca.common.Constants.WEATHER_API_KEY
import com.ss.restaloca.common.Constants.WEATHER_FORECAST_DAYS
import com.ss.restaloca.common.Constants.YELP_API_KEY
import com.ss.restaloca.common.Constants.YELP_ITEMS_LIMIT
import com.ss.restaloca.common.Constants.YELP_RADIUS_METER
import com.ss.restaloca.details.information.InformationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("businesses/search")
    suspend fun getBusinessesByLocation(
        @Query("location") location: String,
        @Query("term") search: String,
        @Query("radius") radius: String = YELP_RADIUS_METER,
        @Query("limit") limit: String = YELP_ITEMS_LIMIT,
        @Header("Authorization") apiKey: String = YELP_API_KEY
    ): Response<BusinessModel>

    @GET("businesses/search")
    suspend fun getBusinessesByLatLng(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("term") search: String,
        @Query("radius") radius: String = YELP_RADIUS_METER,
        @Query("limit") limit: String = YELP_ITEMS_LIMIT,
        @Header("Authorization") apiKey: String = YELP_API_KEY
    ): Response<BusinessModel>

    @GET("businesses/{id}")
    suspend fun getInformation(
        @Path("id") id: String,
        @Header("Authorization") apiKey: String = YELP_API_KEY
    ): Response<InformationModel>

    @GET("businesses/{id}/reviews")
    suspend fun getReviews(
        @Path("id") id: String,
        @Header("Authorization") apiKey: String = YELP_API_KEY
    ): Response<ReviewModel>

    @GET("forecast.json")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("days") days: String = WEATHER_FORECAST_DAYS,
        @Query("key") apiKey: String = WEATHER_API_KEY
    ): Response<WeatherModel>
}