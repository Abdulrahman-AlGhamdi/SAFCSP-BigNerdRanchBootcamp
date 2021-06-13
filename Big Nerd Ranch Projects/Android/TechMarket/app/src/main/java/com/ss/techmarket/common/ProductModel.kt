package com.ss.techmarket.common

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val data: List<Product>,
    val links: Links,
    val meta: Meta
)

data class Product(
    val description: String,
    val id: Int,
    val images: List<String>,
    @SerializedName("is_listed") val isListed: Boolean,
    val price: Int,
    val title: String,
    val tags: MutableList<Tag> = mutableListOf<Tag>(),
    val category: Category,
    val city: City,
    val region: Region?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long
)

data class Link(
    val active: Boolean,
    val label: Any,
    val url: Any
)

data class Links(
    val first: String,
    val last: String,
    val next: String,
    val prev: Any
)

data class Meta(
    @SerializedName("current_page") val currentPage: Int,
    val from: Int,
    @SerializedName("last_page") val lastPage: Int,
    val links: List<Link>,
    val path: String,
    @SerializedName("per_page") val perPage: Int,
    val to: Int,
    val total: Int
)


