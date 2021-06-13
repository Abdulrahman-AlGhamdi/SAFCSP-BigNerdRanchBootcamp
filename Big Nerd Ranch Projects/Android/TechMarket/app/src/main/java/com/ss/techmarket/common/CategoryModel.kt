package com.ss.techmarket.common

data class CategoryResponse(
    val data: List<Category>,
    val links: Links,
    val meta: Meta
)

data class Category(
    val id: Int,
    val title: String,
    val image: String?
)