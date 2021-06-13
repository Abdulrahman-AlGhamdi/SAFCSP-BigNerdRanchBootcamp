package com.ss.restaloca.common

import com.ss.restaloca.common.Constants.WEATHER_BASE_URL
import com.ss.restaloca.common.Constants.YELP_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

    private val businessApi: API = Retrofit.Builder()
        .baseUrl(YELP_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    private val weatherApi: API = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    suspend fun getBusinessesByLocation(location: String, business: String) =
        businessApi.getBusinessesByLocation(location, business)

    suspend fun getBusinessesByLatLng(latitude: String, longitude: String, business: String) =
        businessApi.getBusinessesByLatLng(latitude, longitude, business)

    suspend fun getInformation(id: String) =
        businessApi.getInformation(id)

    suspend fun getReviews(id: String) =
        businessApi.getReviews(id)

    suspend fun getWeather(location: String) =
        weatherApi.getWeather(location)
}