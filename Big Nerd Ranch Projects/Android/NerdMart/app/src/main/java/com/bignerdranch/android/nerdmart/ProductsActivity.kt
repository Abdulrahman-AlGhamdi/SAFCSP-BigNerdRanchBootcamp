package com.bignerdranch.android.nerdmart

import android.content.Context
import android.content.Intent
import android.os.Bundle

class ProductsActivity : NerdMartAbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getFragment() = ProductsFragment()

    companion object {
        fun newIntent(context: Context) = Intent(context, ProductsActivity::class.java)
    }
}
