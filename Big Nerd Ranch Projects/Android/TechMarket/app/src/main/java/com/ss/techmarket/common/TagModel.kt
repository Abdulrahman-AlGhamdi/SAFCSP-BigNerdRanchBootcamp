package com.ss.techmarket.common

import com.google.gson.annotations.SerializedName

data class Tag(
    val id: Int,
    val title: String,
    val image: String?,
    @SerializedName("is_brand") val isBrand: Boolean,
    val parent: Int?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long
)

data class TagResponse(
    val data: List<Tag>,
    val links: Links,
    val meta: Meta
)