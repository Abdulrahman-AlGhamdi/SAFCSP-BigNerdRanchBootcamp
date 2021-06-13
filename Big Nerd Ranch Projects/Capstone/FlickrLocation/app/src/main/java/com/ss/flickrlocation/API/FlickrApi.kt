package com.ss.flickrlocation.API

import com.ss.flickrlocation.Model.Details.DetailsResponse
import com.ss.flickrlocation.Model.Image.PhotoResponse
import com.ss.flickrlocation.Utils.Constant.API_KEY
import com.ss.flickrlocation.Utils.Constant.EXTRAS
import com.ss.flickrlocation.Utils.Constant.FORMAT
import com.ss.flickrlocation.Utils.Constant.NO_JSON_CALLBACK
import com.ss.flickrlocation.Utils.Constant.PHOTO_INFO_URL_END_POINT
import com.ss.flickrlocation.Utils.Constant.RADIUS
import com.ss.flickrlocation.Utils.Constant.RADIUS_UNITS
import com.ss.flickrlocation.Utils.Constant.PHOTO_SEARCH_URL_END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET(PHOTO_SEARCH_URL_END_POINT)
    suspend fun getImages(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("radius") radius: String = RADIUS,
        @Query("radius_units") radiusUnit: String = RADIUS_UNITS,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("extras") date: String = EXTRAS,
        @Query("format") format: String = FORMAT,
        @Query("nojsoncallback") noJsonCallback: String = NO_JSON_CALLBACK
    ): Response<PhotoResponse>

    @GET(PHOTO_INFO_URL_END_POINT)
    suspend fun getImageDetails(
        @Query("photo_id") id: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = FORMAT,
        @Query("nojsoncallback") noJsonCallback: String = NO_JSON_CALLBACK,
    ): Response<DetailsResponse>
}