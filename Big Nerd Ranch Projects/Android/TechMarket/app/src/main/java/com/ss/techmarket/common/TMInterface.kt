package com.ss.techmarket.common

import okhttp3.RequestBody
import retrofit2.http.*

interface TMInterface {

    /**
     * 'title' for searching by the product title
     * 'price_min' for setting the minimum product price
     * 'price_max' for setting the maximum product price
     * 'category' for getting products in category with category id
     * 'tag' for getting products in tag with tag id
     * 'sort' for sorting results by (title, price, created_at, updated_at)
     * 'asc' sort in 'asc' or 'desc' order
     * */

    @GET("products")
    suspend fun productsSearch(
        @Query("page") page: Int,
        @Query("title") title: String? = null,
        @Query("price_min") priceMin: Double? = null,
        @Query("price_max") priceMax: Double? = null,
        @Query("category") category: Int? = null,
        @Query("tag") tag: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("asc") asc: String? = null,
        @Query("region") region: Int?,
        @Query("city") city: Int?
    ): ProductResponse

    @GET("product/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product

    @POST("product")
    suspend fun createPost(@Body requestBody: RequestBody): Product

    @PUT("product/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body requestBody: RequestBody
    ): Product

    @GET("category")
    suspend fun getAllCategories(): CategoryResponse

    @GET("category/{id}")
    suspend fun getCategoryDetails(@Path("id") id: Int): Category

    @GET("tag")
    suspend fun getAllTags(): TagResponse

    @GET("tag/{id}")
    suspend fun getTagDetails(@Path("id") id: Int): Tag

    @GET("tag")
    suspend fun autocompleteTag(@Query("tag") searchQuery: String): TagResponse

    @GET("city")
    suspend fun autocompleteCity(
        @Query("region") region: Int?,
        @Query("name_ar") nameAR: String?,
        @Query("name_en") nameEN: String?
    ): CityResponse

    @GET("city/{id}")
    suspend fun getCity(@Path("id") id: Int): City

    @GET("region")
    suspend fun allRegions(): RegionResponse

    @GET("region/{id}")
    suspend fun getRegion(@Path("id") id: Int): Region
}