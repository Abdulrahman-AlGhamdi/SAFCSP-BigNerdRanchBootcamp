package com.ss.flickrlocation.Repository

import com.ss.flickrlocation.API.FlickrApi
import com.ss.flickrlocation.Database.FlickrDatabase
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.Utils.Constant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FlickrRepository(var database: FlickrDatabase) {

    private val client: OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client.build())
        .build()
        .create(FlickrApi::class.java)

    suspend fun getImages(lat: String, lon: String) =
        api.getImages(lat, lon)

    suspend fun getImageDetails(id: String) =
        api.getImageDetails(id)

    suspend fun addFavoriteAndUpdate(photo: Photo) =
        database.flickrDao().addAndUpdateFavorite(photo)

    suspend fun deleteFavorite(photo: Photo) =
        database.flickrDao().deleteFavorite(photo)

    fun getAllFavorites() =
        database.flickrDao().getAllFavorites()

    suspend fun deleteAllFavorites() =
        database.flickrDao().deleteAllFavorites()
}