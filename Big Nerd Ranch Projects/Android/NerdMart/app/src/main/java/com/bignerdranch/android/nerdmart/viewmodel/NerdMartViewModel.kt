package com.bignerdranch.android.nerdmart.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.bignerdranch.android.nerdmart.R
import com.bignerdranch.android.nerdmartservice.service.payload.Cart
import com.bignerdranch.android.nerdmartservice.service.payload.User
import javax.inject.Inject
import com.bignerdranch.android.nerdmart.BR

class NerdMartViewModel @Inject constructor(
        private val context: Context,
        private var cart: Cart,
        private val user: User
) : BaseObservable() {
    val userGreeting: String
        get() = context.getString(R.string.user_greeting, user.name)

    @get:Bindable
    val cartDisplay: String
        get() = formatCartItemsDisplay()
    private fun formatCartItemsDisplay(): String {
        val numItems = cart.products?.size ?: 0
        return context.resources.getQuantityString(R.plurals.cart, numItems, numItems)
    }

    fun updateCartStatus(cart: Cart) {
        this.cart = cart
        notifyPropertyChanged(BR.cartDisplay)
    }
}
