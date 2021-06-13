package com.ss.restaloca.details.reviews

data class ReviewModel(
    val possible_languages: List<String>,
    val reviews: List<Review>,
    val total: Int
)

data class Review(
    val id: String,
    val rating: Int,
    val text: String,
    val time_created: String,
    val url: String,
    val user: User
)

data class User(
    val id: String,
    val image_url: String?,
    val name: String,
    val profile_url: String
)