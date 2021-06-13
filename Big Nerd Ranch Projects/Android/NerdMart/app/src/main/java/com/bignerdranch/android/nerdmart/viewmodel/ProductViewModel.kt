package com.bignerdranch.android.nerdmart.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import com.bignerdranch.android.nerdmart.R
import com.bignerdranch.android.nerdmartservice.service.payload.Product

class ProductViewModel(context: Context, product: Product, rowNumber: Int
) : BaseObservable() {
    val id: Int = product.id
    val sku: String = product.sku
    val title: String = product.title
    val description: String = product.description
    val displayPrice: String = java.text.NumberFormat.getCurrencyInstance()
            .format(product.priceInCents / 100.0)
    val productUrl: String = product.productUrl
    val productQuantityDisplay: String = context.getString(
            R.string.quantity_display_text, product.backendQuantity)
    val rowColor: Int = androidx.core.content.ContextCompat.getColor(context,
            if (rowNumber % 2 == 0) {
                R.color.white
            } else {
                R.color.light_blue
            })
}
