package com.bignerdranch.android.nerdmart.model

import com.bignerdranch.android.nerdmartservice.service.payload.Cart
import com.bignerdranch.android.nerdmartservice.service.payload.Product
import com.bignerdranch.android.nerdmartservice.service.payload.User
import java.util.*
import javax.inject.Inject

class DataStore private constructor(
        var cachedUser: User,
        var cachedProducts: List<Product>,
        var cachedCart: Cart
) {
    @Inject constructor() : this(User.NO_USER, emptyList(), Cart())

    val cachedAuthToken: UUID
        get() = cachedUser.authToken

    fun clearCache() {
        cachedUser = User.NO_USER
        cachedProducts = emptyList()
        cachedCart = Cart()
    }
}